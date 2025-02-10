package com.ir.formgenerator.service;

import com.ir.formgenerator.model.FileRecord;
import com.ir.formgenerator.repository.FileRepository;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.Instant;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;


@Service
public class FileService  {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // Save file in H2
    public FileRecord saveFile(MultipartFile file) throws IOException {
        Instant start = Instant.now(); 
        FileRecord uploadedFile = new FileRecord();
        uploadedFile.setFileName(file.getOriginalFilename());
        uploadedFile.setFileType(file.getContentType());
        uploadedFile.setFileData(file.getBytes());

        Instant end = Instant.now(); // End timing
        System.out.println("Local Upload Time: " + (end.toEpochMilli() - start.toEpochMilli()) + " ms");
   
        return fileRepository.save(uploadedFile);
    }

     // Download file from H2 by ID
     public File downloadFile(Long fileId) throws IOException {
        FileRecord fileRecord = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found!"));

        // Create a temporary file and write the content of the file stored in the database
        File tempFile = new File(fileRecord.getFileName());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileRecord.getFileData());
        }

        return tempFile;  // Return the file
    }

    public byte[] convertToPdf(Long fileId) throws IOException {
    FileRecord file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("File not found!"));

    ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getFileData());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try (PdfWriter writer = new PdfWriter(outputStream);
         PdfDocument pdfDoc = new PdfDocument(writer);
         Document document = new Document(pdfDoc)) {

        Table table;

        if (file.getFileName().endsWith(".csv")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String firstLine = reader.readLine();

            if (firstLine == null || firstLine.isEmpty()) {
                throw new RuntimeException("CSV file is empty!");
            }

            String[] headers = firstLine.split(",");
            int columnCount = headers.length;
            table = new Table(columnCount);

            // Bold style for headers
            Style boldStyle = new Style().setBold();

            for (String header : headers) {
                table.addCell(new Cell().add(new Paragraph(header).addStyle(boldStyle)));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] cells = line.split(",");
                // Handle missing columns by ensuring blank cells are added
                for (int i = 0; i < columnCount; i++) {
                    String cellValue = (i < cells.length) ? cells[i] : "";
                    table.addCell(new Cell().add(new Paragraph(cellValue)));
                }
            }

        } else {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new RuntimeException("Excel sheet is empty!");
            }

            Row firstRow = sheet.getRow(0);
            int columnCount = firstRow.getPhysicalNumberOfCells();
            table = new Table(columnCount);

            // Bold style for headers
            Style boldStyle = new Style().setBold();

            // Process headers and ensure missing values are treated as blank
            for (int i = 0; i < columnCount; i++) {
                org.apache.poi.ss.usermodel.Cell excelCell = firstRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String headerValue = excelCell.toString().trim().isEmpty() ? "" : excelCell.toString();
                table.addCell(new Cell().add(new Paragraph(headerValue).addStyle(boldStyle)));
            }

            // Process rows and ensure missing cells are treated as blank
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < columnCount; j++) {
                        org.apache.poi.ss.usermodel.Cell excelCell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = excelCell.toString().trim().isEmpty() ? "" : excelCell.toString();
                        table.addCell(new Cell().add(new Paragraph(cellValue)));
                    }
                }
            }
        }

        document.add(table);
    }

    return outputStream.toByteArray();
}

}

