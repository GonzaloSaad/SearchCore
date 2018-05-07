package com.frc.utn.searchcore.model;

import java.io.Serializable;

public class PostListItem implements Serializable, Comparable<PostListItem> {


    private final int DOC_ID;
    private int TF;

    public PostListItem(int id, int tf) {
        this.DOC_ID = id;
        this.TF = tf;
    }

    public int getDocID() {
        return DOC_ID;
    }

    public int getTf() {
        return TF;
    }

    public void addOccurance() {
        TF++;
    }

    @Override
    public int compareTo(PostListItem t) {
        return (getTf() - t.getTf()) < 0 ? 1 : -1;
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
        final PostListItem other = (PostListItem) obj;
        return this.DOC_ID == other.DOC_ID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.DOC_ID ^ (this.DOC_ID >>> 32));
        return hash;
    }


}
