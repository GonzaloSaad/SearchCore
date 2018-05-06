package com.frc.utn.searchcore.io.post;


import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.model.PostEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Cache {

    private int pointer;
    private CachedPostPack[] cache;
    private final int sizeOfCache;

    public Cache(int size) {
        cache = new CachedPostPack[size];
        pointer = 0;
        sizeOfCache = size;
    }

    public abstract Map<String, PostEntry> getPostPack(int file);

    public abstract Map<String, PostEntry> putPostPack(Map<String, PostEntry> postPack, int file);

    public abstract void dump();

    protected CachedPostPack[] getCache(){
        return cache;
    }

    protected CachedPostPack get(int index){
        return cache[index];
    }

    protected void set(CachedPostPack c){
        cache[c.getFile()] = c;
    }

    public void clean() {
        cache = new CachedPostPack[sizeOfCache];
    }

    private void dumpToDisk(CachedPostPack cachedPostPack) {
        PostPackManagement.getInstance().savePostPack(cachedPostPack.getPostPack(), cachedPostPack.getFile());
    }

    public List<Map<String, PostEntry>> getCachedPost() {

        List<Map<String, PostEntry>> list = new ArrayList<>();

        for (CachedPostPack c : cache) {
            if (c != null) {
                list.add(c.getPostPack());
            }
        }
        return list;
    }

    public int size(){
        return cache.length;
    }









}
