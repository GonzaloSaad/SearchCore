/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.files.FileParser;
import com.frc.utn.searchcore.files.FolderFileList;
import com.frc.utn.searchcore.io.management.DocumentMapManagement;
import com.frc.utn.searchcore.io.management.InternalFoldersManagement;
import com.frc.utn.searchcore.io.management.SEQManagement;
import com.frc.utn.searchcore.io.management.VocabularyManagement;
import com.frc.utn.searchcore.model.PostEntry;
import com.frc.utn.searchcore.model.VocabularyEntry;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Gonzalo
 */
public class SearchEngineController {


    private SearchEngineModel searchModel;
    private int indexedTerms = 0;


    private static final Logger logger = Logger.getLogger(SearchEngineController.class.getSimpleName());

    public SearchEngineController() {
        init();
    }

    private void init() {
        Integer SEQ = SEQManagement.getInstance().getSEQ();
        Map<String, VocabularyEntry> voc = VocabularyManagement.getIntance().getVocabulary();
        Map<String, Integer> dmap = DocumentMapManagement.getInstance().getDocumentMap();

        if (SEQ == null || voc == null || dmap == null) {
            logger.log(Level.SEVERE, "Data for model couldnt be retrieved.");
            resetAll();
        } else {
            logger.log(Level.INFO, "Data for model retrieved.");
            searchModel = new SearchEngineModel(voc, dmap, SEQ);
        }
    }

    private void resetAll() {
        InternalFoldersManagement.getIntance().clearAll();
        searchModel = new SearchEngineModel();
    }

    public void indexFolder(String path) throws FileNotFoundException {
        indexedTerms = 0;
        logger.log(Level.INFO, "Starting indexing.");
        FolderFileList fl = new FolderFileList(path);
        for (File f : fl) {
            logger.log(Level.INFO, "Document to ingest: [{0}] \tSize: {1}KB", new Object[]{f.getName(), f.length() / 1000});
            indexFile(f);
        }
        logger.log(Level.INFO, "Terms red [{0}].", indexedTerms);
        commitAllChanges();

    }

    private void commitAllChanges() {
        persistVocabulary();
        persistSEQ();
        persistDocMap();
        searchModel.dump();
    }

    private void indexFile(File file) throws FileNotFoundException {
        boolean shouldSave = false;
        int termsRed = 0;

        Integer docSEQ = getDocumentSEQ(file);

        String text = readAndCleanStopWords(file);

        FileParser fp = new FileParser(text);
        for (String term : fp) {
            if (!term.trim().isEmpty()) {

                termsRed++;
                indexedTerms++;

                if (indexedTerms % DLCConstants.LIMIT_WITHOUT_SAVE == 0) {
                    shouldSave = true;
                }

                PostEntry pe;
                VocabularyEntry ve = searchModel.getFromVocabulary(term);

                if (ve == null) {
                    int postFile = getNextFileIndex();
                    ve = new VocabularyEntry(term, postFile);
                    searchModel.addToVocabulary(ve);

                    pe = new PostEntry(term);
                    pe.addDocument(docSEQ);

                    searchModel.savePostEntry(pe);
                    continue;
                }
                ve.addTermOcurrance();
                pe = searchModel.getPostEntry(term);
                pe.addDocument(docSEQ);

            }
        }
        logger.log(Level.INFO, "Terms red for document [{0}]. Total terms indexed [{1}]", new Object[]{termsRed, indexedTerms});
        if (shouldSave) {
            commitAllChanges();
        }

    }

    private int getDocumentSEQ(File file) {
        Integer docSEQ = searchModel.getFromDocMap(file);
        if (docSEQ == null) {
            docSEQ = searchModel.getSEQ();
            searchModel.addToDocMap(file, docSEQ);
            logger.log(Level.INFO, "Document [{0}] did not exist. Created entry with [{1}] sequence number.", new Object[]{file.getName(), docSEQ});
            searchModel.incrementSEQ();
            //Create document entry;
        } else {
            logger.log(Level.INFO, "Document [{0}] did exist, with [{1}] sequence number.", new Object[]{file.getName(), docSEQ});
        }
        return docSEQ;
    }

    private String readAndCleanStopWords(File file) {
        String text = "";

        try {
            text = ExudeData.getInstance().filterStoppingsKeepDuplicates(FileUtils.readFileToString(file));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (InvalidDataException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return text;
    }

    private int getNextFileIndex() {
        return (searchModel.getVocabulary().size() % DLCConstants.INDEX_CACHE_SIZE);
    }

    private void persistVocabulary() {
        VocabularyManagement.getIntance().saveVocabulary(searchModel.getVocabulary());
    }

    private void persistSEQ() {
        SEQManagement.getInstance().saveSEQ(searchModel.getSEQ());
    }

    private void persistDocMap() {
        DocumentMapManagement.getInstance().saveDocumentMap(searchModel.getDocMap());
    }


}
