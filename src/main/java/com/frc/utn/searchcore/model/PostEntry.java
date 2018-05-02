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
 *
 * @author Gonzalo
 */
public class PostEntry implements Serializable {

    private final String TERM;
    private final int INITIAL_OCCURRENCE = 1;
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

    public boolean addDocument(int docID) {

        if (containsDocument(docID)) {
            addTermOccuranceToDoc(docID);
            return false;
        }
        
        DOC_HASH_MAP.put(docID, INITIAL_OCCURRENCE);
        return true;
    }
    
    private boolean containsDocument(Integer docID){
        return DOC_HASH_MAP.get(docID)!=null;
    }
    
    private void addTermOccuranceToDoc(int docID) {
        int occurance = getMap().get(docID);
        occurance++;
        getMap().put(docID, occurance);
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
            return getTf() - t.getTf();
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
    
    private class PostItemComparator implements Comparator<PostItem>{

        @Override
        public int compare(PostItem t, PostItem t1) {
            return t.compareTo(t1);
        }
        
    }
}
