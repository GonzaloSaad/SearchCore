/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;
import com.frc.utn.searchcore.model.DocumentResult;
import org.apache.commons.lang.StringUtils;


/**
 * @author Gonzalo
 */
public class DocumentManagement {

    private static final String DOCUMENTS_PATH = "src/main/resources/dlc/docs/doc";
    private static final String DOCUMENT_EXTENSION = ".dlc";
    private static DocumentManagement instance;

    private DocumentManagement() {

    }

    public static DocumentManagement getInstance() {
        if (instance == null) {
            instance = new DocumentManagement();
        }
        return instance;
    }

    public DocumentResult getDocument(int docId) {
        DLCObjectReader<DocumentResult> or = new DLCObjectReader<>();
        return or.read(createPath(docId));
    }

    public void saveDocument(DocumentResult doc) {
        DLCObjectWriter<DocumentResult> ow = new DLCObjectWriter<>();
        ow.write(doc, createPath(doc.getDocID()));
    }

    private String createPath(int docId) {
        return DOCUMENTS_PATH + StringUtils.leftPad(Integer.toString(docId),3,"0") + DOCUMENT_EXTENSION;
    }
}
