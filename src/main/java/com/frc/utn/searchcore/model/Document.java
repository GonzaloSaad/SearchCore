/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.model;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author gonzalo.saad
 */
public class Document implements Serializable {
    private final int docID;
    private final String name;
    private final String url;

    public Document(String name, String url, int docID) {
        this.name = name;
        this.url = url;
        this.docID = docID;
    }

    public Document(File file, int docID){
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
