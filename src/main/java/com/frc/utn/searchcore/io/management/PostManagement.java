/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import com.frc.utn.searchcore.io.management.post.Cache;
import com.frc.utn.searchcore.io.management.post.PostPackManagement;
import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;
import com.frc.utn.searchcore.model.PostEntry;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class PostManagement {

    private static final Logger logger = Logger.getLogger(PostManagement.class.getSimpleName());
    private int termAddedWithOutSaving;
    private final int LIMIT_WITHOUT_SAVING = 10000;
    private final int CACHE_SIZE = 1000;
    private final int POST_FILES = 1000;
    private final Cache cache;
    private Map<String, Integer> index;

    private static PostManagement instance;

    private PostManagement() {
        cache = new Cache(CACHE_SIZE);
        termAddedWithOutSaving = 0;
        index = PostPackManagement.getInstance().getIndex();
        if (index == null){
            index = new HashMap<>();
            PostPackManagement.getInstance().saveIndex(index);
        }
    }

    public static PostManagement getInstance() {
        if (instance == null) {
            instance = new PostManagement();
        }
        return instance;
    }

    public PostEntry getPostForTerm(String term) {
        //logger.log(Level.INFO, "Term to retrieve post list [{0}].", term);


        PostEntry pe;

        Integer file = getFileNumberForTerm(term);

        if (file == null) {
            //logger.log(Level.INFO, "Term was not found.");
            return null;
        }
        //logger.log(Level.INFO, "Term's post list is in file ["+ file+"].");

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            pe = postPack.get(term);
            if (pe != null) {
                //logger.log(Level.INFO, "Post list for term was found.");
                return pe;
            }
        }

        //logger.log(Level.INFO, "Term's post list was not found.");
        return null;
    }

    public void addPostForTerm(PostEntry pe) {
        String term = pe.getTerm();
        Integer file = getFileNumberForTerm(term);

        if (file == null) {
            file = getNextFileNumber();
            addTermToIndex(term,file);

        }

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            postPack.put(term, pe);
        }
    }

    public void dump(){
        logger.log(Level.INFO,"Saving index and cache to storage.");
        PostPackManagement.getInstance().saveIndex(index);
        cache.dumpToDisk();
        termAddedWithOutSaving = 0;
    }

    private Map<String, PostEntry> getPostPack(int file) {
        //logger.log(Level.INFO, "Looking in cache.");
        Map<String, PostEntry> postPack = cache.getPostPack(file);

        if (postPack == null) {
            //logger.log(Level.INFO, "Post file was not in cache. Looking in storage.");
            postPack = PostPackManagement.getInstance().getPostPack(file);

            if (postPack==null){
                postPack = new HashMap<>();
                //logger.log(Level.INFO, "Post file was not in storage. Creating new one.");
            }

            cache.putPostPack(postPack,file);
            //logger.log(Level.INFO, "New post file added to cache.");
        }
        return postPack;
    }

    private void addTermToIndex(String term, int fileNumber){
        index.put(term,fileNumber);
        termAddedWithOutSaving++;
        /*if (termAddedWithOutSaving == LIMIT_WITHOUT_SAVING){
            dump();
        }*/
    }

    private Integer getFileNumberForTerm(String term){
        return index.get(term);
    }

    private int getNextFileNumber(){
        return (index.size()) % POST_FILES;
    }




}
