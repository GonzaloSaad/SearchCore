/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.io.management.PostPackManagement;
import com.frc.utn.searchcore.io.cache.Cache;
import com.frc.utn.searchcore.io.cache.IntermediateCache;
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
public class IndexHelper {

    private static Logger logger = Logger.getLogger(IndexHelper.class.getName());
    private Cache cache;

    public IndexHelper() {
        startCache();
    }

    private void startCache() {
        cache = new IntermediateCache(DLCConstants.INDEX_CACHE_SIZE);
    }

    private int getNextFileIndex() {
        return (EngineModel.getInstance().getVocabulary().size() % DLCConstants.POST_FILES);
    }

    private int getNextDocumentID() {
        return EngineModel.getInstance().getDocMap().size();
    }

    public PostEntry getPostEntry(String term) {
        PostEntry pe = null;

        int file = EngineModel.getInstance().getFromVocabulary(term).getPostFile();

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

    public VocabularyEntry getVocabularyEntryForTerm(String term) {

        VocabularyEntry ve = EngineModel.getInstance().getFromVocabulary(term);

        if (ve == null) {
            int postFile = getNextFileIndex();
            ve = new VocabularyEntry(term, postFile);
            EngineModel.getInstance().addToVocabulary(ve);
        }

        return ve;
    }

    public int getDocumentID(File file) {
        Integer docID = EngineModel.getInstance().getFromDocMap(file);
        if (docID == null) {
            docID = getNextDocumentID();
            EngineModel.getInstance().addToDocMap(file, docID);
            logger.log(Level.INFO, "Document [{0}] did not exist. Created entry with [{1}] sequence number.", new Object[]{file.getName(), docID});
        } else {
            logger.log(Level.INFO, "Document [{0}] did exist, with [{1}] sequence number.", new Object[]{file.getName(), docID});
        }
        return docID;
    }

    private Map<String, PostEntry> getPostPack(int file) {
        Map<String, PostEntry> postPack = cache.getPostPack(file);

        if (postPack == null) {
            postPack = new HashMap<>();
            cache.putPostPack(postPack, file);
        }
        return postPack;
    }

    public void commit(boolean parallel) {
        logger.log(Level.INFO, "Saving indexes and cache to storage.");
        cache.dump(parallel);
        EngineModel.getInstance().commit();
    }

    public void commit() {
        commit(true);
    }

    public void finishIndexing() {

        commit(false);
        logger.log(Level.INFO, "Finalize indexing...");

        Runnable job1 = new UpdatingNrJob(0, DLCConstants.POST_FILES / 2);
        Runnable job2 = new UpdatingNrJob(DLCConstants.POST_FILES / 2, DLCConstants.POST_FILES);


        Thread thread = new Thread(job1);
        thread.start();

        job2.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.log(Level.INFO, "Done.");

    }

    private class UpdatingNrJob implements Runnable {
        private int start;
        private int end;


        public UpdatingNrJob(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            doUpdate();

        }

        public void doUpdate() {
            for (int i = start; i < end; i++) {
                Map<String, PostEntry> postPack = PostPackManagement.getInstance().getPostPack(i);
                if (postPack == null) {
                    throw new RuntimeException("The model was not consistent.");
                }

                for (String term : postPack.keySet()) {
                    VocabularyEntry ve = EngineModel.getInstance().getFromVocabulary(term);
                    if (ve == null) {
                        throw new RuntimeException("The model was not consistent.");
                    }
                    ve.updateNrValue(postPack.get(term));
                }

            }
        }
    }


}
