package com.frc.utn.searchcore.io.cache;

import com.frc.utn.searchcore.DLCConstants;
import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.model.PostList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SearchCache extends Cache {

    private int pointer;
    private Map<Integer, Integer> cacheMap;

    public SearchCache(int size) {
        super(size);
        cacheMap = new HashMap<>();
        pointer = 0;
    }

    @Override
    public Map<String, PostList> getPostPack(int file) {
        CachedPostPack cachedPostPack;
        CachedPostPack storedPostPack;

        Integer indexOfCache = cacheMap.get(file);

        if (indexOfCache != null) {
            cachedPostPack = get(indexOfCache);
            cachedPostPack.markUsed();
            return cachedPostPack.getPostPack();
        }


        int indexOfLessUsedCache = getLessUsedPostPackIndex();


        cachedPostPack = get(indexOfLessUsedCache);
        if (cachedPostPack !=null) {
            cacheMap.put(cachedPostPack.getFile(), null);
        }

        storedPostPack = getPostPackFromStorage(file);
        set(storedPostPack, indexOfLessUsedCache);
        cacheMap.put(storedPostPack.getFile(), indexOfLessUsedCache);



        return storedPostPack.getPostPack();
    }

    private CachedPostPack getPostPackFromStorage(int file) {
        Map<String, PostList> postPack = PostPackManagement.getInstance().getPostPack(file);
        if (postPack == null) {
            throw new IllegalStateException("The file was not found! Inconsistency in the model!");
        }

        return new CachedPostPack(file, postPack);
    }

    public int getLessUsedPostPackIndex() {

        int index = pointer;

        do {
            CachedPostPack cpp = get(index);

            if (cpp == null || !cpp.used()) {
                pointer = index;
                return index;
            } else {
                cpp.markNotUsed();
            }
            index = (index + 1) % size();

        } while (true);
    }

    @Override
    public Map<String, PostList> putPostPack(Map<String, PostList> postPack, int file) {
        throw new UnsupportedOperationException("Search Cache cant add files.");
    }

    @Override
    public void dump(boolean parallel) {
        clean();
    }

    public double occupacy() {
        int cached = 0;
        for (CachedPostPack c : getCache()) {
            if (c != null) {
                cached++;
            }
        }
        return (double) cached / (double) size();
    }
}


