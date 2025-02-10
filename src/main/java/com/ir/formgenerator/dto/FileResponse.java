package com.ir.formgenerator.dto;

public class FileResponse {
    private String fileName;
    private String url;
    private String message;

    public FileResponse(String fileName, String url, String message) {
        this.fileName = fileName;
        this.url = url;
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }
}

