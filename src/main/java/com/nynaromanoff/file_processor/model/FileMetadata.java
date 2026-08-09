package com.nynaromanoff.file_processor.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;
    private String s3Key;
    private String traceId;

    @Enumerated(EnumType.STRING)
    private FileStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

