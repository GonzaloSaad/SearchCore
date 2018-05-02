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
    private static Logger logger = Logger.getLogger(Cache.class.getName());

    public Cache(int size){
        cache = new CachedPostPack[size];
        pointer = 0;
    }

    public int size(){
        return cache.length;
    }

    public Map<String,PostEntry> getPostPack(int file){


        for (CachedPostPack c: cache){
            if (c !=null && c.getFile() == file){
                logger.log(Level.INFO,"File [{0}] in cache.",file);
                c.markUsed();
                return c.getPostPack();
            }
        }
        logger.log(Level.INFO,"File [{0}] not in cache.",file);
        return null;
    }

    public int getLessUsedPostPackIndex(){

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
    }

    public Map<String,PostEntry> putPostPack(Map<String,PostEntry> postPack, int file){
        int index = getLessUsedPostPackIndex();


        CachedPostPack out = cache[index];
        CachedPostPack cpp = new CachedPostPack(file,postPack);
        cache[index] = cpp;


        if (out !=null){
            dumpToDisk(out);
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

    public void dumpToDisk(){
        for (CachedPostPack c: cache){
            if (c !=null){
                logger.log(Level.INFO,"Dumping file [{0}].",c.getFile());
                dumpToDisk(c);
            }
        }
    }

    private void dumpToDisk(CachedPostPack cachedPostPack){
        PostPackManagement.getInstance().savePostPack(cachedPostPack.getPostPack(),cachedPostPack.getFile());
    }

}
