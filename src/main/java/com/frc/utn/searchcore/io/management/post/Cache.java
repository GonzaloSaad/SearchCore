package com.frc.utn.searchcore.io.management.post;


import com.frc.utn.searchcore.model.PostEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cache {

    private int pointer;
    private CachedPostPack[] cache;
    private final int sizeOfCache;
    private static Logger logger = Logger.getLogger(Cache.class.getName());

    public Cache(int size){
        cache = new CachedPostPack[size];
        pointer = 0;
        sizeOfCache = size;
    }

    public int size(){
        return cache.length;
    }

    public int operationSize(){
        int sum = 0;
        for (CachedPostPack c: cache){
            if (c !=null){
                sum += c.getPostPack().size();
            }
        }
        return sum;
    }

    public Map<String,PostEntry> getPostPack(int file){
        CachedPostPack c = cache[file];

        if (c == null){
            return null;
        }

        c.markModified();
        return c.getPostPack();
    }

    /*public int getLessUsedPostPackIndex(){

        int index = pointer;

        do{
            CachedPostPack cpp = cache[index];

            if (cpp == null || !cpp.used()){
                pointer = index;
                return index;
            } else {
                cpp.markNotUsed();
            }
            index = (index+1) % size();

        } while (true);
    }*/

    public Map<String,PostEntry> putPostPack(Map<String,PostEntry> postPack, int file){

        CachedPostPack out = cache[file];
        CachedPostPack cpp = new CachedPostPack(file,postPack);
        cache[file] = cpp;

        if (out !=null){
            return out.getPostPack();
        }
        return null;
    }

    public List<Map<String,PostEntry>> getCachedPost(){

        List<Map<String,PostEntry>> list = new ArrayList<>();

        for (CachedPostPack c: cache){
            if (c !=null){
                list.add(c.getPostPack());
            }
        }
        return list;
    }


    private void dumpToDisk(CachedPostPack cachedPostPack){
        PostPackManagement.getInstance().savePostPack(cachedPostPack.getPostPack(),cachedPostPack.getFile());
    }

    public void dump(){
        mergePostPacks();
        clean();
    }

    public void clean(){
        cache = new CachedPostPack[sizeOfCache];
    }

    private void mergePostPacks(){
        int merged = 0;
        for (int i = 0; i<cache.length; i++){
            CachedPostPack cachedPostPack = cache[i];
            if(cachedPostPack == null || !cachedPostPack.wasModified()){
                continue;
            }
            merged++;
            Map<String,PostEntry> postPack = cachedPostPack.getPostPack();
            int file = cachedPostPack.getFile();

            Map<String,PostEntry> diskPostPack = PostPackManagement.getInstance().getPostPack(i);
            if (diskPostPack==null || diskPostPack.size() ==0){
                PostPackManagement.getInstance().savePostPack(postPack,i);
                continue;
            }

            for (String key: postPack.keySet()){
                PostEntry dpe = diskPostPack.get(key);
                PostEntry cpe = postPack.get(key);

                if (dpe == null){
                    diskPostPack.put(cpe.getTerm(),cpe);
                    continue;
                }

                dpe.mergePostEntry(cpe);
            }
            PostPackManagement.getInstance().savePostPack(diskPostPack,i);

        }
        logger.log(Level.INFO,"Merged [{0}] post packs",merged);


    }



}
