# File Upload and S3 Integration Service

## Overview

The File Upload and S3 Integration Service is a Spring Boot-based RESTful service that provides functionalities for file upload to an Amazon S3 bucket (or MinIO for local development), file conversion (to PDF), and downloading the converted files. The service exposes endpoints for these operations and is designed to handle both success and failure scenarios during file uploads.

### Key Features:
- **Upload Files to S3**: Upload files to a specified S3 bucket or MinIO.
- **Convert Files to PDF**: Convert uploaded files to PDF format.
- **Download Converted Files**: Download the converted PDF files.
- **Unit Tests**: Includes unit tests to validate the file upload and conversion process, handling both successful and failure cases.

---

## Requirements

- **Java**: Version 21 or higher
- **Maven**: For dependency management and building the project
- **Spring Boot**: Framework used to build the application
- **Amazon S3 or MinIO**: For remote and local file storage
- **Postman/REST client**: For testing the API endpoints
- **Docker**: (Optional) For running MinIO locally
- **PgAdmin**: (Optional) For running Postgres DB locally

---
 

## Installation

 
#### For MinIO (Local Development):
In `src/main/resources/application.properties`, set the following properties for local MinIO setup:
```properties
aws.s3.endpoint=http://localhost:9000
aws.s3.accessKey=minioaccesskey
aws.s3.secretKey=miniosecretkey
aws.s3.bucketName=formgenerator-bucket
aws.s3.region=us-east-1
```

### 3. Build and Run the Application

To build the project using Maven, run:
```bash
mvn clean install
```

After building the project, run the application using:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

---

## Endpoints

### 1. Upload a File to S3
- **URL**: `POST /files/upload/s3`
- **Method**: POST
- **Description**: Uploads a file to S3 (or MinIO).
- **Parameters**: 
  - `file`: The file to be uploaded.
- **Response**:
  - `200 OK`: If the file is successfully uploaded.
  - `500 Internal Server Error`: If there is an issue during the upload.

   

### 2. Convert a File to PDF and Download
- **URL**: `GET /files/convert/{fileId}`
- **Method**: GET
- **Description**: Converts an uploaded file to PDF and returns it as a downloadable file.
- **Response**:
  - `200 OK`: The converted PDF file.
  - `500 Internal Server Error`: If there is an error during file conversion.

**Example:**
Using `curl` to convert and download a file:
```bash
curl -X GET http://localhost:8080/files/convert/12345 --output converted.pdf
```

---

## Running Tests

To run unit tests, execute the following Maven command:
```bash
mvn test
```

The tests will verify that the file upload, PDF conversion, and failure scenarios are handled correctly.

To run a specific test class, use:
```bash
mvn -Dtest=com.ir.formgenerator.controller.FileControllerTest test
```

---
  
## License

This project is licensed under SparkDigital Property License - contact Muhluri Mhlongo for details.
