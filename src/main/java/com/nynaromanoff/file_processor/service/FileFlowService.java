package com.nynaromanoff.file_processor.service;

import io.opentelemetry.api.trace.Span;
import com.nynaromanoff.file_processor.dto.UploadRequest;
import com.nynaromanoff.file_processor.dto.UploadResponse;
import com.nynaromanoff.file_processor.model.FileMetadata;
import com.nynaromanoff.file_processor.model.FileStatus;
import com.nynaromanoff.file_processor.repository.FileMetadataRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileFlowService {
    private final FileMetadataRepository repository;
    private final StorageService storageService;
    private final SqsClient sqsClient;
    private final String queueName = "minha-fila";

    public FileFlowService(FileMetadataRepository repository, StorageService storageService, SqsClient sqsClient) {
        this.repository = repository;
        this.storageService = storageService;
        this.sqsClient = sqsClient;
    }

    public UploadResponse initiateUploadFlow(UploadRequest request) {
        // Pega o Trace ID automático injetado pelo agente do OpenTelemetry
        String currentTraceId = Span.current().getSpanContext().getTraceId();

        String s3Key = "uploads/" + UUID.randomUUID() + "-" + request.getFileName();

        FileMetadata metadata = FileMetadata.builder()
                .fileName(request.getFileName())
                .s3Key(s3Key)
                .status(FileStatus.PENDING_UPLOAD)
                .traceId(currentTraceId)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(metadata);

        String uploadUrl = storageService.generatePresignedUrl(s3Key);

        // Dispara mensagem em formato JSON para a fila
        String jsonPayload = String.format("{\"fileId\":\"%s\",\"s3Key\":\"%s\"}", metadata.getId(), s3Key);

        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueName)
                .messageBody(jsonPayload)
                .build());

        return new UploadResponse(metadata.getId(), uploadUrl);
    }
}
