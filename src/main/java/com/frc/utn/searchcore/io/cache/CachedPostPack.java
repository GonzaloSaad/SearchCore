package com.frc.utn.searchcore.io.cache;

import com.frc.utn.searchcore.model.PostEntry;

import java.util.Map;

public class CachedPostPack {
    private int file;
    private Map<String, PostEntry> postPack;
    private boolean used = true;

    public CachedPostPack(int file, Map<String, PostEntry> postPack) {
        this.file = file;
        this.postPack = postPack;

    }

    public int getFile() {
        return file;
    }

    public Map<String, PostEntry> getPostPack() {
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