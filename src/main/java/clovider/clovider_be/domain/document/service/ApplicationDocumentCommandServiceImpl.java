package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.repository.ApplicationDocumentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationDocumentCommandServiceImpl implements ApplicationDocumentCommandService {
    private final ApplicationDocumentRepository applicationDocumentRepository;

    @Override
    public void createApplicationDocuments(List<String> imageUrls, Application application) {
        imageUrls.forEach(imageUrl -> applicationDocumentRepository.save(Document.builder()
                .image(imageUrl)
                .application(application).build()));
    }
}
