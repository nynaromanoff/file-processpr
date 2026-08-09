package com.nynaromanoff.file_processor.service;

import com.nynaromanoff.file_processor.repository.FileMetadataRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileWorker {
    private static final Logger log = LoggerFactory.getLogger(FileWorker.class);
    private final FileMetadataRepository repository;

    public FileWorker(FileMetadataRepository repository) {
        this.repository = repository;
    }

    @SqsListener("minha-fila")
    public void processFileMessage(String messageJson) {
        log.info("Mensagem recebida da fila para processamento: {}", messageJson);

        try {
            if (messageJson.contains("corrompido")){
                throw new IllegalArgumentException("Arquivo inválido ou corrompido.");
            } else {
                log.info("Processamento concluído com sucesso.");
            }

        } catch (Exception e) {
            log.error("Erro fatal ao processar o arquivo. Enviando metadados para auditoria.", e);
            throw e;
        }
    }
}
