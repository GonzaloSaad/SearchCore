/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.management;

import com.frc.utn.searchcore.io.util.DLCObjectReader;
import com.frc.utn.searchcore.io.util.DLCObjectWriter;

/**
 *
 * @author Gonzalo
 */
public class SEQManagement {
    
    public static final String DOC_SEQ_PATH = "src/main/resources/dlc/SEQ.dlc";
    private static SEQManagement instance;
    
    private SEQManagement(){        
    }
    
    public static SEQManagement getInstance(){
        if (instance == null){
            instance = new SEQManagement();
        }
        return instance;
    }        
    
    public Integer getSEQ(){
        DLCObjectReader<Integer> or = new DLCObjectReader<>();
        return or.read(DOC_SEQ_PATH);
    }   
        
    public void saveSEQ(Integer integer){
        DLCObjectWriter<Integer> ow = new DLCObjectWriter();
        ow.write(integer, DOC_SEQ_PATH);
    }
}
