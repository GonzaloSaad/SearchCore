/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;

import com.frc.utn.searchcore.files.FileParser;
import com.frc.utn.searchcore.files.FolderFileList;
import com.frc.utn.searchcore.model.DocumentResult;
import com.frc.utn.searchcore.model.PostEntry;
import com.frc.utn.searchcore.model.VocabularyEntry;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Gonzalo
 */
public class SearchEngineController {

    private static final Logger logger = Logger.getLogger(SearchEngineController.class.getSimpleName());
    private static final SearchHelper searchHelper = new SearchHelper();

    public SearchEngineController() {

    }

    public void indexFolder(String path)  {
        logger.log(Level.INFO, "Starting indexing.");

        IndexHelper indexHelper = new IndexHelper();

        int indexedTerms = 0;
        long sizeOfIndexed = 0;

        FolderFileList fl = new FolderFileList(path);

        for (File f : fl) {
            long sizeOfFile = f.length()/1000;
            sizeOfIndexed += sizeOfFile;
            logger.log(Level.INFO, "Document to ingest: [{0}] \tSize: {1}KB\tTotal: {2}KB.", new Object[]{f.getName(), sizeOfFile,sizeOfIndexed});


            boolean shouldSave = false;
            int termsRed = 0;

            Integer docID = indexHelper.getDocumentID(f);

            String text = readAndCleanStopWords(f);
            FileParser fp = new FileParser(text);

            for (String term : fp) {
                if (!term.trim().isEmpty()) {

                    termsRed++;
                    indexedTerms++;

                    if (indexedTerms % DLCConstants.LIMIT_WITHOUT_SAVE == 0) {
                        shouldSave = true;
                    }

                    VocabularyEntry ve = indexHelper.getVocabularyEntryForTerm(term);
                    PostEntry pe = indexHelper.getPostEntry(term);

                    ve.addTermOcurrance();
                    pe.addDocument(docID);

                }
            }
            logger.log(Level.INFO, "Terms red for document [{0}]. Total terms indexed [{1}]", new Object[]{termsRed, indexedTerms});
            if (shouldSave) {
                indexHelper.commit();
            }
        }
        logger.log(Level.INFO, "Terms red [{0}].", indexedTerms);
        indexHelper.finishIndexing();

    }

    private String readAndCleanStopWords(File file) {
        String text = "";

        try {
            text = ExudeData.getInstance().filterStoppingsKeepDuplicates(FileUtils.readFileToString(file));
        } catch (IOException | InvalidDataException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return text;
    }

    private String read(File file) {
        String text = "";

        try {
            text = FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }



    public List<DocumentResult> getDocumentsForQuery(String query){
        return null;
    }


}
