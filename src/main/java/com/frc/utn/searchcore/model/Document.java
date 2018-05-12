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
    private final String ID;
    private final String name;
    private final String downloadLink;
    private final String viewLink;
    private int DLCID;

    public Document(String ID, String name, String downloadLink, String viewLink, int id) {
        this.ID = ID;
        this.name = name;
        this.downloadLink = downloadLink;
        this.viewLink = viewLink;
        this.DLCID = id;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getViewLink() {
        return viewLink;
    }

    public int getDLCID() {
        return DLCID;
    }

    public void setDLCID(int DLCID) {
        this.DLCID = DLCID;
    }
    
}
