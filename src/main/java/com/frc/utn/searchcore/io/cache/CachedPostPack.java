package com.frc.utn.searchcore.io.cache;

import com.frc.utn.searchcore.model.PostList;

import java.util.Map;

public class CachedPostPack {
    private int file;
    private Map<String, PostList> postPack;
    private boolean used = true;

    public CachedPostPack(int file, Map<String, PostList> postPack) {
        this.file = file;
        this.postPack = postPack;

    }

    public int getFile() {
        return file;
    }

    public Map<String, PostList> getPostPack() {
        return postPack;
    }

    public void markUsed() {
        used = true;
    }

    public void markNotUsed() {
        used = false;
    }

    public boolean used() {
        return used;
    }

}