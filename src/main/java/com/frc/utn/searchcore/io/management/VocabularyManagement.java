/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;
import com.frc.utn.searchcore.model.VocabularyEntry;
import java.util.Map;

/**
 *
 * @author Gonzalo
 */
public class VocabularyManagement {

    public static final String VOCABULARY_FILE_PATH = "src/main/resources/dlc/vocabulary.v";
    private static VocabularyManagement instance;

    private VocabularyManagement() {

    }

    public static VocabularyManagement getIntance() {
        if (instance == null) {
            instance = new VocabularyManagement();
        }
        return instance;
    }

    public Map<String, VocabularyEntry> getVocabulary() {
        DLCObjectReader< Map<String, VocabularyEntry>> or = new DLCObjectReader();
        return or.read(VOCABULARY_FILE_PATH);
    }

    public void saveVocabulary(Map<String, VocabularyEntry> vocabulary) {
        DLCObjectWriter< Map<String, VocabularyEntry>> ow = new DLCObjectWriter();
        ow.write(vocabulary, VOCABULARY_FILE_PATH);
    }

}
