package com.ir.formgenerator.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String csvFile;
    private String pdfFile;
    private LocalDateTime timestamp;
    private long localTime;
    private long s3Time;

    // Constructor
    public AuditLog(String username, String csvFile, String pdfFile, LocalDateTime timestamp, long localTime, long s3Time) {
        this.username = username;
        this.csvFile = csvFile;
        this.pdfFile = pdfFile;
        this.timestamp = timestamp;
        this.localTime = localTime;
        this.s3Time = s3Time;
    }

    // Default constructor (needed for JPA)
    public AuditLog() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(long localTime) {
        this.localTime = localTime;
    }

    public long getS3Time() {
        return s3Time;
    }

    public void setS3Time(long s3Time) {
        this.s3Time = s3Time;
    }
}
