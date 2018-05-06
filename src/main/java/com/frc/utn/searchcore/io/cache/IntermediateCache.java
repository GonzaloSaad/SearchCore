package com.frc.utn.searchcore.io.post;

import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.model.PostEntry;

import java.util.Map;

public class IntermediateCache extends Cache {

    public IntermediateCache(int size) {
        super(size);
    }

    @Override
    public Map<String, PostEntry> getPostPack(int file) {
        CachedPostPack c = get(file);

        if (c == null) {
            return null;
        }

        c.markModified();
        return c.getPostPack();
    }

    @Override
    public Map<String, PostEntry> putPostPack(Map<String, PostEntry> postPack, int file) {

        CachedPostPack out = get(file);
        CachedPostPack cpp = new CachedPostPack(file, postPack);
        set(cpp);

        if (out != null) {
            return out.getPostPack();
        }
        return null;
    }

    @Override
    public void dump() {
        mergePostPacks();
        clean();
    }

    private void mergePostPacks() {
        new PersistentThread(getCache()).start();
    }

    private class PersistentThread extends Thread {

        private CachedPostPack[] threadCache;

        public PersistentThread(CachedPostPack[] cache) {
            threadCache = cache;
        }


        public void run() {
            doMerge();
        }


        private void doMerge() {
            for (int i = 0; i < threadCache.length; i++) {

                CachedPostPack cachedPostPack = threadCache[i];

                if (cachedPostPack == null || !cachedPostPack.wasModified()) {
                    continue;
                }
                Map<String, PostEntry> postPack = cachedPostPack.getPostPack();

                Map<String, PostEntry> diskPostPack = PostPackManagement.getInstance().getPostPack(i);
                if (diskPostPack == null || diskPostPack.size() == 0) {
                    PostPackManagement.getInstance().savePostPack(postPack, i);
                    continue;
                }

                for (String key : postPack.keySet()) {
                    PostEntry dpe = diskPostPack.get(key);
                    PostEntry cpe = postPack.get(key);

                    if (dpe == null) {
                        diskPostPack.put(cpe.getTerm(), cpe);
                        continue;
                    }

                    dpe.mergePostEntry(cpe);
                }
                PostPackManagement.getInstance().savePostPack(diskPostPack, i);
            }

        }


    }
}
