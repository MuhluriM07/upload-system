package com.ir.formgenerator.service;

import java.io.File;
import java.io.IOException;

public interface FileStore {
    void uploadFile(File file) throws IOException;       // Method to upload file
    File downloadFile(String fileName) throws IOException;  // Method to download file
}