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
public class PostEntry implements Serializable {

    private final String TERM;

    private final Map<Integer, Integer> DOC_HASH_MAP;

    public PostEntry(String term) {
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

    public void mergePostEntry(PostEntry otherPE) {
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

    public Set<PostItem> getListOfDocument() {
        Set<PostItem> set = new TreeSet<>(new PostItemComparator());

        for (Integer l : getMap().keySet()) {
            PostItem pi = new PostItem(l, getMap().get(l));
            set.add(pi);
        }
        return set;
    }

    private class PostItem implements Serializable, Comparable<PostItem> {

        private final int DOC_ID;
        private int TF;

        public PostItem(int id, int tf) {
            this.DOC_ID = id;
            this.TF = tf;
        }

        public long getDocID() {
            return DOC_ID;
        }

        public int getTf() {
            return TF;
        }

        public void addOccurance() {
            TF++;
        }

        @Override
        public int compareTo(PostItem t) {
            return (getTf() - t.getTf())<0? -1 : 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PostItem other = (PostItem) obj;
            return this.DOC_ID == other.DOC_ID;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + (int) (this.DOC_ID ^ (this.DOC_ID >>> 32));
            return hash;
        }

    }

    private class PostItemComparator implements Comparator<PostItem> {

        @Override
        public int compare(PostItem t, PostItem t1) {
            return t.compareTo(t1);
        }

    }

    public int getNr(){
        return DOC_HASH_MAP.size();
    }
}
