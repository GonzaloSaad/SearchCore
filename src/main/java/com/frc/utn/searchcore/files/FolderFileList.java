/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.files;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gonzalo
 */
public class FolderFileList implements Iterable<File> {

    FilenameFilter fileFilter = new FilenameFilter() {
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".txt");
    }};
    
    private final ArrayList<File> FOLDER_FILE_LIST;

    public FolderFileList(String path) {
        this(new File(path));
    }
    
    public FolderFileList(File folder){
        FOLDER_FILE_LIST = listFilesForFolder(folder);
    }

    private ArrayList<File> listFilesForFolder(File folder) {

        ArrayList<File> files = new ArrayList();

        for (final File fileEntry : folder.listFiles(fileFilter)) {
            if (!fileEntry.isDirectory()) {
                files.add(fileEntry);
            }
        }
        return files;
    }

    @Override
    public Iterator<File> iterator() {
        return FOLDER_FILE_LIST.iterator();
    }

}
