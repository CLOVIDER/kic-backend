package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.repository.ApplicationDocumentRepository;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationDocumentCommandServiceImpl implements ApplicationDocumentCommandService {
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final ApplicationQueryService applicationQueryService;

    @Override
    public void createApplicationDocuments(Map<DocumentType, String> fileUrls, Application application) {

        fileUrls.forEach((documentType, fileUrl) -> {
            Document document = Document.builder()
                    .image(fileUrl)
                    .application(application)
                    .documentType(documentType)
                    .isAccept(Accept.WAIT)
                    .build();
            applicationDocumentRepository.save(document);
        });
    }

    @Override
    public void updateApplicationDocuments(Map<DocumentType, String> fileUrls, Application application){

        fileUrls.forEach((documentType, fileUrl) -> {
            Document document = applicationDocumentRepository.findByApplicationAndDocumentType(application, documentType)
                    .orElseThrow(() -> new ApiException(ErrorStatus._DOCUMENT_NOT_FOUND));

            document.changeFileUrl(fileUrl);

            applicationDocumentRepository.save(document);
        });
    }

    @Override
    public void acceptDocument(Long documentId, Accept accept){
        Document document = applicationDocumentRepository.findById(documentId).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );

        document.changeAccept(accept);
    }
}
