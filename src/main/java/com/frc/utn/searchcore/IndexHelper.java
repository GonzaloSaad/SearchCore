/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.io.management.*;
import com.frc.utn.searchcore.io.management.post.Cache;
import com.frc.utn.searchcore.io.management.post.IntermediateCache;
import com.frc.utn.searchcore.io.management.post.SearchCache;
import com.frc.utn.searchcore.model.DocumentResult;
import com.frc.utn.searchcore.model.PostEntry;
import com.frc.utn.searchcore.model.VocabularyEntry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class IndexEngineModel {

    private static Logger logger = Logger.getLogger(IndexEngineModel.class.getName());



    private Cache cache;


    public IndexEngineModel() {
        startCache();
    }

    private void startCache() {
        cache = new IntermediateCache(DLCConstants.INDEX_CACHE_SIZE);
    }

    public int getDocumentID(File file) {
        Integer docID = EngineModel.getInstance().getFromDocMap(file);
        if (docID == null) {
            docID = getNextID();
            addToDocMap(file, docID);
            logger.log(Level.INFO, "Document [{0}] did not exist. Created entry with [{1}] sequence number.", new Object[]{file.getName(), docID});
        } else {
            logger.log(Level.INFO, "Document [{0}] did exist, with [{1}] sequence number.", new Object[]{file.getName(), docID});
        }
        return docID;
    }


    public int getNextID() {
        return getDocMap().size();
    }

    public void savePostEntry(PostEntry pe) {
        String term = pe.getTerm();
        int file = getFromVocabulary(term).getPostFile();

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            postPack.put(term, pe);
        } else {
            throw new RuntimeException("Error in cache.");
        }
    }

    public PostEntry getPostEntry(String term) {
        PostEntry pe = null;

        int file = getFromVocabulary(term).getPostFile();

        Map<String, PostEntry> postPack = getPostPack(file);

        if (postPack != null) {
            pe = postPack.get(term);
            if (pe == null) {
                pe = new PostEntry(term);
                postPack.put(term, pe);
            }
        }

        return pe;
    }

    private Map<String, PostEntry> getPostPack(int file) {
        Map<String, PostEntry> postPack = cache.getPostPack(file);

        if (postPack == null) {
            postPack = new HashMap<>();
            intercache.putPostPack(postPack, file);
        }
        return postPack;
    }

    public void dump() {
        logger.log(Level.INFO, "Saving index and cache to storage.");
        persistDocMap();
        persistVocabulary();
        cache.dump();

    }


    }





    public void finishIndexing() {
        for (VocabularyEntry ve: VOCABULARY.values()){

        }
    }




}
