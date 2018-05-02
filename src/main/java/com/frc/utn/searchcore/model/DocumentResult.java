/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.model;

/**
 *
 * @author gonzalo.saad
 */
public class DocumentResult {
    private String name;
    private String url;
    private String summary;

    public DocumentResult(String name, String url, String summary) {
        this.name = name;
        this.url = url;
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }
    
    
}
