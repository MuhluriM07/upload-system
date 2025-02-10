package com.ir.formgenerator.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ir.formgenerator.config.S3Service;
import com.ir.formgenerator.model.FileRecord;
import com.ir.formgenerator.service.FileService;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    private final S3Service s3Service;

   @Autowired // ✅ Constructor injection
    public FileController(FileService fileService, S3Service s3Service) {
        this.fileService = fileService;
        this.s3Service = s3Service;
    }

    // Upload a file to h2
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileRecord uploadedFile = fileService.saveFile(file);
            return ResponseEntity.ok("File uploaded successfully! File ID: " + uploadedFile.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

     // ✅ Upload a file to S3
     @PostMapping("/upload/s3")
     public ResponseEntity<String> uploadFileToS3(@RequestParam("file") MultipartFile file) {
         try {
             // Convert MultipartFile to File
             File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
             file.transferTo(tempFile);
 
             // Upload to S3
             s3Service.uploadFileToS3(file.getOriginalFilename(), tempFile);
 
             return ResponseEntity.ok("File uploaded to S3 successfully!");
         } catch (IOException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("S3 upload failed");
         }
     }

    

    // Convert and download PDF
    @GetMapping("/convert/{fileId}")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long fileId) {
        try {
            byte[] pdfData = fileService.convertToPdf(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.pdf\"")
                    .body(pdfData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
