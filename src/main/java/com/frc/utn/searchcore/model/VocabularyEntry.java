/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.model;

import java.io.Serializable;

/**
 *
 * @author Gonzalo
 */
public class VocabularyEntry implements Serializable {

    private final String TERM;
    private final int POST_FILE;
    private int TF;
    private int Nr;

    public int getPostFile() {
        return POST_FILE;
    }

    public VocabularyEntry(String term, int file) {
        TERM = term;
        TF = 1;
        Nr = 1;
        POST_FILE = file;

    }

    public String getTerm() {
        return TERM;
    }

    public void addTermOcurrance() {
        TF++;
    }

    public int getTF() {
        return TF;
    }

    public int getNr() {
        return Nr;
    }

    public void updateNrValue(PostEntry postEntry){
        this.Nr = postEntry.getNr();
    }

}
