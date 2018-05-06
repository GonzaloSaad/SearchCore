/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.utn.searchcore.files;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class FileParser implements Iterable<String>, Closeable {

    Scanner sc;


    public FileParser(File file) throws FileNotFoundException {
        sc = new Scanner(file, "ISO-8859-1");

    }

    public FileParser(String text) {
        sc = new Scanner(text);
    }

    @Override
    public Iterator<String> iterator() {
        return new FileIterator();
    }

    @Override
    public void close() throws IOException {
        sc.close();
    }

    private class FileIterator implements Iterator<String> {

        @Override
        public boolean hasNext() {

            return sc.hasNext();
        }

        @Override
        public String next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No quedan mas palabras.");
            }

            return FileParser.cleanString(sc.next()).toLowerCase();

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("No se puede alterar el contenido del archivo."); //To change body of generated methods, choose Tools | Templates.
        }

    }


    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }


    public static String stripNonAlphabetic(String s) {
        return s.replaceAll("([a-z]+[0-9]+|[0-9]+[a-z]+)[a-z0-9]*\b|[^A-Za-z]+", "");
    }


    public static String cleanString(String s) {
        return stripNonAlphabetic(stripAccents(s));
    }

}
