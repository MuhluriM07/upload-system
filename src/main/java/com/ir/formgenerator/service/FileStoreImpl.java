package com.ir.formgenerator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileStoreImpl implements FileStore {

    private static final String UPLOAD_DIR = "uploads/"; // Directory to store files locally

    public FileStoreImpl() {
        // Create the upload directory if it does not exist
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Upload a file to the server's local storage.
     * @param file Multipart file uploaded by the user.
     * @return Path of the uploaded file.
     * @throws IOException If an error occurs during file upload.
     */
    @Override
    public void uploadFile(File file) throws IOException {
        Path targetPath = Path.of(UPLOAD_DIR, file.getName());

        try (InputStream inputStream = new FileInputStream(file)) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("File uploaded successfully: " + targetPath.toString());
    }

    /**
     * Upload a file (overloaded method to handle MultipartFile).
     * @param file Multipart file uploaded via HTTP request.
     * @return Path of the uploaded file.
     * @throws IOException If an error occurs during file upload.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        Path targetPath = Path.of(UPLOAD_DIR, file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return targetPath.toString();
    }

    /**
     * Download a file from the server's local storage.
     * @param fileName Name of the file to be downloaded.
     * @return File object pointing to the downloaded file.
     * @throws FileNotFoundException If the file does not exist.
     */
    @Override
    public File downloadFile(String fileName) throws FileNotFoundException {
        File file = new File(UPLOAD_DIR + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return file;
    }
}
