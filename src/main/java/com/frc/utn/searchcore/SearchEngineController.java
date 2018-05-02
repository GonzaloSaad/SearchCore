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

    private int indexedTerms =0;
    private static final int LIMIT_WITHOUT_SAVE = 100000;
    private SearchEngineModel searchModel;
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

            logger.log(Level.INFO, "Document to ingest: [{0}]", f.getName());
            Instant start = Instant.now();
            indexFile(f);
            Instant end = Instant.now();
            long nanoseconds = Duration.between(start,end).getNano();
            if (nanoseconds==0){
                nanoseconds=1;
            }
            long fileSize = f.length();
            logger.log(Level.INFO,"Size: {0}KB\tTime: {1}ms\tSpeed: {2} KB/s",new Object[]{fileSize/1000, nanoseconds/1000000, (fileSize*1000000/nanoseconds)});
        }
        logger.log(Level.INFO,"Terms red [{0}].",indexedTerms);
        commitAllChanges();

    }

    private void commitAllChanges(){
        persistVocabulary();
        persistSEQ();
        persistDocMap();
        searchModel.finish();
        indexedTerms=0;
    }

    private void indexFile(File file) throws FileNotFoundException {

        int termsReaded = 0;

        Integer docSEQ = getDocumentSEQ(file);

        String text = readAndCleanStopWords(file);

        FileParser fp = new FileParser(text);
        for (String term : fp) {
            if (!term.trim().isEmpty()) {
                //logger.log(Level.INFO, "Term to ingest: [{0}]", term);
                PostEntry pe;
                VocabularyEntry ve = searchModel.getFromVocabulary(term);
                termsReaded++;
                indexedTerms++;
                if (ve == null) {

                    ve = new VocabularyEntry(term);
                    searchModel.addToVocabulary(ve);
                    //logger.log(Level.INFO, "Term [{0}] did not exist. Created VocabularyEntry for it.", term);

                    pe = new PostEntry(term);
                    pe.addDocument(docSEQ);
                    //logger.log(Level.INFO, "PostEntry created.");

                    //logger.log(Level.INFO, "Persisting new post.");
                    searchModel.persistPostEntry(pe);


                    continue;
                }
                //logger.log(Level.INFO, "Term [{0}] did exist.", term);
                ve.addTermOcurrance();
                pe = searchModel.getPostEntry(term);
                boolean documentNotExisted = pe.addDocument(docSEQ);
                if (documentNotExisted) {
                    //logger.log(Level.INFO, "Document [{0}] did not exist for term [{1}].", new Object[]{file.getName(), term});
                    ve.addDocumentAppearance();
                }


            }

        }

        logger.log(Level.INFO,"[{0}] terms red for document [{1}].",new Object[]{termsReaded,file.getName()});
        if (indexedTerms == LIMIT_WITHOUT_SAVE){
            commitAllChanges();
        }

    }

    private int getDocumentSEQ(File file){
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
