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

// Map<DocumentType, String> imageUrls를 순회
        imageUrls.forEach((documentType, imageUrl) -> {
            // documentType은 DocumentType 열거형의 값
            Document document = Document.builder()
                    .image(imageUrl)  // 이미지 URL 설정
                    .application(application)  // 관련된 신청 정보 설정
                    .documentType(documentType)  // 문서 유형 설정
                    .build();
            applicationDocumentRepository.save(document);
        });
    }
}
