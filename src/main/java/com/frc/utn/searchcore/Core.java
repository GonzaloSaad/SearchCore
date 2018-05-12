/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;





import java.io.*;

import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class Core {
    public static void main(String[] args) {
        String path = "C:\\Users\\Gonzalo\\Desktop\\t2";
        String query = "This etext was prepared by the PG Shakespeare Team, a team of about twenty Project Gutenberg volunteers.";
        String folderUID = "0B_R7SeoAotsmUUtYendIX04zRjA";




        try {
            InputStream stream = Core.class.getResourceAsStream("/logging.properties");
            System.out.println(stream);
            LogManager.getLogManager().readConfiguration(stream);
            SearchEngineController controller = new SearchEngineController();
            //controller.indexFolder(folderUID);
            controller.getDocumentsForQuery(query);

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }/* catch (GeneralSecurityException e) {
            e.printStackTrace();
        }*/
    }
}
