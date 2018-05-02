/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.io.management.PostManagement;
import com.frc.utn.searchcore.model.PostEntry;
import com.frc.utn.searchcore.model.VocabularyEntry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class SearchEngineModel {

    private final Map<String, Integer> DOC_ID_MAP;
    private final Map<String, VocabularyEntry> VOCABULARY;
    private int SEQ;

    public SearchEngineModel(Map<String, VocabularyEntry> vocabulary, Map<String, Integer> dmap, int seq) {
        this.VOCABULARY = vocabulary;
        this.SEQ = seq;
        this.DOC_ID_MAP = dmap;
    }

    public SearchEngineModel() {
        this.VOCABULARY = new HashMap<>();
        this.DOC_ID_MAP = new HashMap<>();
        this.SEQ = 0;
    }

    public Map<String, Integer> getDocMap() {
        return DOC_ID_MAP;
    }

    public Map<String, VocabularyEntry> getVocabulary() {
        return VOCABULARY;
    }

    public int getSEQ() {
        return SEQ;
    }

    public void incrementSEQ() {
        SEQ++;
    }

    public VocabularyEntry getFromVocabulary(String term) {
        return VOCABULARY.get(term);
    }

    public void addToVocabulary(VocabularyEntry ve) {
        VOCABULARY.put(ve.getTerm(), ve);
    }

    public Integer getFromDocMap(File file) {
        return DOC_ID_MAP.get(file.getName());
    }

    public void addToDocMap(File file, int docSEQ) {
        DOC_ID_MAP.put(file.getName(), docSEQ);
    }

    public void persistPostEntry(PostEntry pe) {
        PostManagement.getInstance().addPostForTerm(pe);
    }

    public PostEntry getPostEntry(String term) {
        return PostManagement.getInstance().getPostForTerm((term));
    }

    public void finish(){
        PostManagement.getInstance().dump();
    }


}
