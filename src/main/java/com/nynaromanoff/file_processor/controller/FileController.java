package com.nynaromanoff.file_processor.controller;

import com.nynaromanoff.file_processor.dto.UploadRequest;
import com.nynaromanoff.file_processor.dto.UploadResponse;
import com.nynaromanoff.file_processor.service.FileFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    private final FileFlowService fileFlowService;

    public FileController(FileFlowService fileFlowService) {
        this.fileFlowService = fileFlowService;
    }

    @PostMapping("/upload-request")
    public ResponseEntity<UploadResponse> requestUpload(@RequestBody UploadRequest request) {
        UploadResponse response = fileFlowService.initiateUploadFlow(request);
        return ResponseEntity.accepted().body(response);
    }

}
