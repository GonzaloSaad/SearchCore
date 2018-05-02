/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

/**
 *
 * @author Gonzalo
 */
public class InternalFoldersManagement {
    private static InternalFoldersManagement instance;
    
    
    private InternalFoldersManagement(){
        
    }
    
    public static InternalFoldersManagement getIntance(){
        if (instance == null){
            instance = new InternalFoldersManagement();
        }
        return instance;
    }
    
    public void clearAll(){
        
    }
}
