package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
import java.util.List;
import java.util.Map;

public interface ApplicationDocumentCommandService {
    void createApplicationDocuments(Map<DocumentType, String> imageUrls, Application application);
    void acceptDocument(Long documentId, Accept accept);
}
