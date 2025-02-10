package com.ir.formgenerator.service;

import com.ir.formgenerator.model.FileRecord;
import com.ir.formgenerator.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StorageService {

    private final FileRepository fileRepository;

    public StorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileRecord storeFile(MultipartFile file) {
        try {
            FileRecord fileRecord = new FileRecord();
            fileRecord.setFileName(file.getOriginalFilename());
            fileRecord.setFileData(file.getBytes());
            return fileRepository.save(fileRecord);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public List<FileRecord> getAllFiles() {
        return fileRepository.findAll();
    }
}
