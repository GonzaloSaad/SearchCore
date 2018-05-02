/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.io.util;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * @author Gonzalo
 */
public class DLCObjectReader<T> {

    public DLCObjectReader() {

    }

    public T read(String path) {
        Object o;

        try (FileInputStream istream = new FileInputStream(path)) {
            ObjectInputStream p = new ObjectInputStream(istream);

            o = p.readObject();

            p.close();
        } catch (Exception e) {
            o = null;
        }

        return (T) o;
    }
}
