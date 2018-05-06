package com.frc.utn.searchcore.io.cache;

import com.frc.utn.searchcore.DLCConstants;
import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.model.PostEntry;
import java.util.Arrays;
import java.util.Map;

public class SearchCache extends Cache {

    private int pointer;
    private boolean[] cacheMap;

    public SearchCache(int size) {
        super(size);
        cacheMap = new boolean[DLCConstants.INDEX_CACHE_SIZE];
        pointer = 0;
        Arrays.fill(cacheMap, false);
    }

    private boolean isCached(int file) {
        return cacheMap[file];
    }

    private void setInCache(int file){
        cacheMap[file] = true;
    }

    private void setOutOfCache(int file){
        cacheMap[file] = false;
    }

    public Map<String, PostEntry> getPostPack(int file) {
        CachedPostPack cpp;
        CachedPostPack spp;

        if (isCached(file)) {
            cpp = get(file);
            cpp.markUsed();
            return cpp.getPostPack();
        }

        spp = getPostPackFromStorage(file);
        int index = getLessUsedPostPackIndex();

        if (get(index) != null){
            setOutOfCache(get(index).getFile());
        }

        set(spp);
        setInCache(file);
        return spp.getPostPack();
    }

    private CachedPostPack getPostPackFromStorage(int file) {
        Map<String, PostEntry> postPack = PostPackManagement.getInstance().getPostPack(file);
        if (postPack == null){
            throw new IllegalStateException("The file was not found! Inconsistency in the model!");
        }

        return new CachedPostPack(file,postPack);
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

    public Map<String, PostEntry> putPostPack(Map<String, PostEntry> postPack, int file) {
        throw new UnsupportedOperationException("Search Cache cant add files.");
    }

    public void dump() {
        clean();
    }
}


