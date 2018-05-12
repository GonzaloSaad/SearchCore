package com.frc.utn.searchcore.googledrive;

import com.google.api.services.drive.model.File;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GoogleDriveDowloader {


    public static String download(File file) throws IOException {
        String encoding = "UTF-8";

        URL url = new URL(file.getWebContentLink());
        URLConnection urlCon = url.openConnection();
        InputStream is = urlCon.getInputStream();

        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        is.close();
        return result.toString();
    }
}
