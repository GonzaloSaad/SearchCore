/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Gonzalo
 */
public class Core {
    public static void main(String[] args) {
        String path = "C:\\Users\\Gonzalo\\Desktop\\t1";
        String query = "This etext was prepared by the PG Shakespeare Team, a team of about twenty Project Gutenberg volunteers.";
        

        try {
            InputStream stream = Core.class.getResourceAsStream("/logging.properties");
            System.out.println(stream);
            LogManager.getLogManager().readConfiguration(stream);
            SearchEngineController controller = new SearchEngineController();
            controller.indexFolder(path);
            controller.getDocumentsForQuery(query);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
