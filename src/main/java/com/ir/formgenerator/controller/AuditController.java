package com.ir.formgenerator.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ir.formgenerator.controller.*;
import com.ir.formgenerator.model.AuditLog;
import com.ir.formgenerator.repository.AuditRepository;
import com.ir.formgenerator.service.PdfGenerationService;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final PdfGenerationService pdfService;
    private final AuditRepository auditRepository;

    public AuditController(PdfGenerationService pdfService, AuditRepository auditRepository) {
        this.pdfService = pdfService;
        this.auditRepository = auditRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Logic to store file and log audit record
        return ResponseEntity.ok("File uploaded successfully.");
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        List<AuditLog> logs = auditRepository.findAll();
        return ResponseEntity.ok(logs);
    }
}

