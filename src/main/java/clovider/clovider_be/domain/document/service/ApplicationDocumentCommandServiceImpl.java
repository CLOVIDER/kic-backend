package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.repository.ApplicationDocumentRepository;
import clovider.clovider_be.domain.enums.DocumentType;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationDocumentCommandServiceImpl implements ApplicationDocumentCommandService {
    private final ApplicationDocumentRepository applicationDocumentRepository;

    @Override
    public void createApplicationDocuments(Map<DocumentType, String> imageUrls, Application application) {

        if (imageUrls == null) {
            imageUrls = new HashMap<>();
        }

        imageUrls.forEach((documentType, imageUrl) -> {
            Document document = Document.builder()
                    .image(imageUrl)
                    .application(application)
                    .documentType(documentType)
                    .build();
            applicationDocumentRepository.save(document);
        });
    }
}
