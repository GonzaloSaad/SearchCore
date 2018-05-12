package com.frc.utn.searchcore.googledrive;

import com.frc.utn.searchcore.io.GoogleDrive;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GoogleDriveFileList implements Iterable<File> {

    private static final String APPLICATION_NAME = "BuscadorDLC";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FOLDER = "credentials"; // Directory to store user credentials.
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CLIENT_SECRET_DIR = "./client_secret.json";
    private static final String ONLY_TXT_REGEX = ".+\\.txt";
    private final List<File> DOCUMENT_LIST;

    public GoogleDriveFileList(String folderUID) throws IOException, GeneralSecurityException {
        DOCUMENT_LIST = getListOfFilesInGoogleDriveFolder(folderUID);
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleDrive.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_DIR);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static List<File> getListOfFilesInGoogleDriveFolder(String folderUID) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        FileList result = service.files().list().setQ("'" + folderUID + "' in parents")
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, webContentLink, webViewLink)")
                .execute();

        List<File> files = new ArrayList<>();
        for (File f : result.getFiles()) {
            if (f.getName().matches(ONLY_TXT_REGEX)) {
                files.add(f);
            }
        }


        return files;
    }

    @Override
    public Iterator<File> iterator() {
        return DOCUMENT_LIST.iterator();
    }
}
