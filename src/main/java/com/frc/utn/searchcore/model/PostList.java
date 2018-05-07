/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Gonzalo
 */
public class PostList implements Serializable {

    private final String TERM;

    private final Map<Integer, Integer> DOC_HASH_MAP;

    public PostList(String term) {
        TERM = term;
        DOC_HASH_MAP = new HashMap<>();
    }

    public String getTerm() {
        return TERM;
    }

    public Map<Integer, Integer> getMap() {
        return DOC_HASH_MAP;
    }

    public void addDocument(int docID) {
        addDocument(docID, 1);
    }

    public void addDocument(int docID, int initialOccurrence) {

        if (containsDocument(docID)) {
            addTermOccurenceToDoc(docID);
            return;
        }
        DOC_HASH_MAP.put(docID, initialOccurrence);

    }

    public Integer getDocumentTF(int docID) {
        return DOC_HASH_MAP.get(docID);
    }

    private boolean containsDocument(Integer docID) {
        return DOC_HASH_MAP.get(docID) != null;
    }

    private void addTermOccurenceToDoc(int docID) {
        addTermOccurenceToDoc(docID, 1);
    }

    private void addTermOccurenceToDoc(int docID, int amount) {
        Integer occurance = getMap().get(docID);
        occurance += amount;
        getMap().put(docID, occurance);
    }

    public void mergePostEntry(PostList otherPE) {
        Map<Integer, Integer> otherMap = otherPE.getMap();

        for (int docId : otherMap.keySet()) {

            Integer docTF = getDocumentTF(docId);

            if (docTF == null) {
                addDocument(docId, otherMap.get(docId));
                continue;
            }
            addTermOccurenceToDoc(docId, otherMap.get(docId));
        }
    }

    public Set<PostListItem> getListOfDocument() {
        Set<PostListItem> set = new TreeSet<>(new PostItemComparator());

        for (Integer l : getMap().keySet()) {
            PostListItem pi = new PostListItem(l, getMap().get(l));
            set.add(pi);
        }
        return set;
    }



    private class PostItemComparator implements Comparator<PostListItem> {

        @Override
        public int compare(PostListItem t, PostListItem t1) {
            return t.compareTo(t1);
        }

    }

    public int getNr(){
        return DOC_HASH_MAP.size();
    }
}
