package com.ir.formgenerator.service;

import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ir.formgenerator.config.S3Service;
import com.ir.formgenerator.util.FileUtils;


@Service
public class FileTransferService {
    private final S3Service s3Service;
    private final FileStore fileStore;

    public FileTransferService(S3Service s3Service, FileStore fileStore) {
        this.s3Service = s3Service;
        this.fileStore = fileStore;
    }

    public void transferFile(String filePath) throws IOException {
        File file = new File(filePath);

        // Convert File to MultipartFile
        MultipartFile multipartFile = FileUtils.convertToMultipartFile(file);

        // Measure transfer to on-premise store
        long startTimeOnPrem = System.nanoTime();
        fileStore.uploadFile(file);  // Assuming `fileStore.uploadFile()` uploads to on-premise store
        long endTimeOnPrem = System.nanoTime();
        long onPremDuration = endTimeOnPrem - startTimeOnPrem;
        System.out.println("File transfer to on-premise store took: " + onPremDuration + " ns");

        // Measure transfer to S3
        long startTimeS3 = System.nanoTime();
        s3Service.uploadFileToS3(file.getName(), file);
        long endTimeS3 = System.nanoTime();
        long s3Duration = endTimeS3 - startTimeS3;
        System.out.println("File transfer to S3 took: " + s3Duration + " ns");
    }
}
