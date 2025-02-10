package com.ir.formgenerator.service;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.ir.formgenerator.model.AuditLog;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final RabbitTemplate rabbitTemplate;
    
    public void logFileProcessing(String username, String csvFile, String pdfFile, long localTime, long s3Time) {
        AuditLog log = new AuditLog(username, csvFile, pdfFile, LocalDateTime.now(), localTime, s3Time);
        rabbitTemplate.convertAndSend("auditQueue", log);
    }
}

