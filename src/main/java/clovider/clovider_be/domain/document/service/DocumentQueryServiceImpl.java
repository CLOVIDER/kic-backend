package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.dto.DocumentResponse;
import clovider.clovider_be.domain.document.dto.DocumentResponse.DocumentInfo;
import clovider.clovider_be.domain.document.repository.ApplicationDocumentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentQueryServiceImpl implements DocumentQueryService{

    private final ApplicationDocumentRepository applicationDocumentRepository;

    @Override
    public List<DocumentInfo> getDocumentInfoByApplicationId(Long applicationId) {
        List<Document> documents = applicationDocumentRepository.findByApplicationId(
                applicationId);

        return documents.stream()
                .map(DocumentResponse::toDocumentInfo)
                .toList();
    }
}
