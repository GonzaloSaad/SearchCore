/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.model;

import java.io.File;

/**
 *
 * @author gonzalo.saad
 */
public class DocumentResult {
    private final int docID;
    private final String name;
    private final String url;

    public DocumentResult(String name, String url, int docID) {
        this.name = name;
        this.url = url;
        this.docID = docID;
    }

    public DocumentResult(File file, int docID){
        this(file.getName(),file.getAbsolutePath(),docID);
    }

    public int getDocID() {
        return docID;
    }

    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }
    
    
}
