/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.io.management.post.Cache;
import com.frc.utn.searchcore.model.PostEntry;
import com.frc.utn.searchcore.model.VocabularyEntry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class SearchEngineModel {

    private final Map<String, Integer> DOC_ID_MAP;
    private final Map<String, VocabularyEntry> VOCABULARY;
    private static Logger logger = Logger.getLogger(SearchEngineModel.class.getName());

    private int SEQ;
    private Cache cache;

    public SearchEngineModel(Map<String, VocabularyEntry> vocabulary, Map<String, Integer> dmap, int seq) {
        this.VOCABULARY = vocabulary;
        this.SEQ = seq;
        this.DOC_ID_MAP = dmap;
        this.cache = new Cache(DLCConstants.INDEX_CACHE_SIZE);
    }

    public SearchEngineModel() {
        this(new HashMap<>(), new HashMap<>(), 0);
    }

    public Map<String, Integer> getDocMap() {
        return DOC_ID_MAP;
    }

    public Integer getFromDocMap(File file) {
        return DOC_ID_MAP.get(file.getName());
    }

    public void addToDocMap(File file, int docSEQ) {
        DOC_ID_MAP.put(file.getName(), docSEQ);
    }

    public Map<String, VocabularyEntry> getVocabulary() {
        return VOCABULARY;
    }

    public VocabularyEntry getFromVocabulary(String term) {
        return VOCABULARY.get(term);
    }

    public void addToVocabulary(VocabularyEntry ve) {
        VOCABULARY.put(ve.getTerm(), ve);
    }

    public int getSEQ() {
        return SEQ;
    }

    public void incrementSEQ() {
        SEQ++;
    }

    public void savePostEntry(PostEntry pe) {
        String term = pe.getTerm();
        int file = getFromVocabulary(term).getPostFile();

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            postPack.put(term, pe);
        } else {
            throw new RuntimeException("Error in cache.");
        }

    }

    public PostEntry getPostEntry(String term) {
        PostEntry pe = null;

        int file = getFromVocabulary(term).getPostFile();

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            pe = postPack.get(term);
            if (pe == null) {
                pe = new PostEntry(term);
                postPack.put(term,pe);
            }
        }

        return pe;
    }

    private Map<String, PostEntry> getPostPack(int file) {
        Map<String, PostEntry> postPack = cache.getPostPack(file);

        if (postPack == null) {
            postPack = new HashMap<>();
            cache.putPostPack(postPack, file);
        }
        return postPack;
    }

    public void dump() {
        logger.log(Level.INFO, "Saving index and cache to storage.");
        cache.dump();

    }


}
