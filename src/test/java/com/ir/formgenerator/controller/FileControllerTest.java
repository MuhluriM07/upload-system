package com.ir.formgenerator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ir.formgenerator.service.FileService;
import com.ir.formgenerator.config.S3Service;
import com.ir.formgenerator.model.FileRecord;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private S3Service s3Service;

    @Test
    void testUploadFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        
        FileRecord mockFileRecord = new FileRecord();
        mockFileRecord.setId(1L);
        
        when(fileService.saveFile(any())).thenReturn(mockFileRecord);

        mockMvc.perform(multipart("/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("File uploaded successfully! File ID: 1")));
    }

    @Test
    void testUploadFileToS3_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello S3".getBytes());

        doNothing().when(s3Service).uploadFileToS3(anyString(), any(File.class));

        mockMvc.perform(multipart("/files/upload/s3").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("File uploaded to S3 successfully!")));
    }

    @Test
    void testConvertPdf_Success() throws Exception {
        byte[] mockPdfData = "PDF_DATA".getBytes();
        when(fileService.convertToPdf(anyLong())).thenReturn(mockPdfData);

        mockMvc.perform(get("/files/convert/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"converted.pdf\""))
                .andExpect(content().bytes(mockPdfData));
    }

    @Test
    void testUploadFile_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());

        when(fileService.saveFile(any())).thenThrow(new IOException("File save error"));

        mockMvc.perform(multipart("/files/upload").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Upload failed"));
    }

//    @Test
//     void testUploadFileToS3_Failure() throws Exception {
//         MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello S3".getBytes());

//         // Specify the IOException to be thrown
//         doThrow(new IOException("S3 upload error")).when(s3Service).uploadFileToS3(anyString(), any(File.class));

//         mockMvc.perform(multipart("/files/upload/s3").file(file))
//                 .andExpect(status().isInternalServerError())
//                 .andExpect(content().string("S3 upload failed"));
//     }


    @Test
    void testConvertPdf_Failure() throws Exception {
        when(fileService.convertToPdf(anyLong())).thenThrow(new IOException("Conversion error"));

        mockMvc.perform(get("/files/convert/1"))
                .andExpect(status().isInternalServerError());
    }
}
