/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class InternalFoldersManagement {
    private static InternalFoldersManagement instance;
    private static final Logger logger = Logger.getLogger(InternalFoldersManagement.class.getName());
    private static final File ROOT_FILE = new File("src/main/resources/dlc");

    private InternalFoldersManagement() {

    }

    public static InternalFoldersManagement getInstance() {
        if (instance == null) {
            instance = new InternalFoldersManagement();
        }
        return instance;
    }

    public void clearAll() {

        try {
            delete(ROOT_FILE);
            logger.log(Level.INFO, "All files erased.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear files, system will not be consistent");
        }
    }

    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            logger.log(Level.INFO, "Cleaning [{0}]", f.getName());
            for (File c : f.listFiles())
                delete(c);
        } else {
            if (!f.delete()) {
                throw new FileNotFoundException("Failed to delete file: " + f);
            }
        }
    }
}
