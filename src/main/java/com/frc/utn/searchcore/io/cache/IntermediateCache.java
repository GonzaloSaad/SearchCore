package com.frc.utn.searchcore.io.cache;

import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.model.PostList;
import java.util.Map;

public class IntermediateCache extends Cache {

    Thread persistingThread = new Thread();

    public IntermediateCache(int size) {
        super(size);
    }

    @Override
    public Map<String, PostList> getPostPack(int file) {
        CachedPostPack c = get(file);

        if (c == null) {
            return null;
        }

        return c.getPostPack();
    }

    @Override
    public Map<String, PostList> putPostPack(Map<String, PostList> postPack, int file) {

        CachedPostPack out = get(file);
        CachedPostPack cpp = new CachedPostPack(file, postPack);
        set(cpp,cpp.getFile());

        if (out != null) {
            return out.getPostPack();
        }
        return null;
    }

    @Override
    public void dump(boolean parallel) {
        mergePostPacks(parallel);
        clean();
    }

    private void mergePostPacks(boolean parallel) {

        try {
            persistingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Runnable job = new PersistentJob(getCache());
        if (parallel){
            persistingThread = new Thread(job);
            persistingThread.start();
        } else {
            job.run();
        }
    }

    private class PersistentJob implements Runnable {

        private CachedPostPack[] threadCache;

        public PersistentJob(CachedPostPack[] cache) {
            threadCache = cache;
        }


        public void run() {
            doMerge();
        }


        private void doMerge() {
            for (int i = 0; i < threadCache.length; i++) {

                CachedPostPack cachedPostPack = threadCache[i];

                if (cachedPostPack == null) {
                    continue;
                }
                Map<String, PostList> postPack = cachedPostPack.getPostPack();

                Map<String, PostList> diskPostPack = PostPackManagement.getInstance().getPostPack(i);
                if (diskPostPack == null || diskPostPack.size() == 0) {
                    PostPackManagement.getInstance().savePostPack(postPack, i);
                    continue;
                }

                for (String key : postPack.keySet()) {
                    PostList dpe = diskPostPack.get(key);
                    PostList cpe = postPack.get(key);

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
