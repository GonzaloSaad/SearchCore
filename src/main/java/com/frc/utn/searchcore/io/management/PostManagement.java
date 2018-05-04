/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import com.frc.utn.searchcore.DLCConstants;
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
    private final Cache cache;

    private static PostManagement instance;

    private PostManagement() {
        cache = new Cache(DLCConstants.INDEX_CACHE_SIZE);
    }

    public static PostManagement getInstance() {
        if (instance == null) {
            instance = new PostManagement();
        }
        return instance;
    }

   /* public PostEntry getPostForTerm(String term) {

        PostEntry pe = null;

        Integer file = getFileNumberForTerm(term);

        if (file == null) {
            return null;
        }

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            pe = postPack.get(term);
            if (pe == null) {
                pe = new PostEntry(term);
            }
        }

        return pe;
    }

    public void addPostForTerm(PostEntry pe) {
        String term = pe.getTerm();
        Integer file = getFileNumberForTerm(term);

        if (file == null) {
            file = getNextFileNumber();
            addTermToIndex(term, file);

        }

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            postPack.put(term, pe);
        } else {
            throw new RuntimeException("Error in cache.");
        }
    }

    public void dump() {
        logger.log(Level.INFO, "Saving index and cache to storage.");
        cache.dump();
    }

    private Map<String, PostEntry> getPostPack(int file) {
        Map<String, PostEntry> postPack = cache.getPostPack(file);

        if (postPack == null) {
            postPack = new HashMap<>();
            cache.putPostPack(postPack, file);
        }
        return postPack;
    }*/

}
