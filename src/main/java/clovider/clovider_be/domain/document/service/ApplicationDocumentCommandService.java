package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.application.Application;
import java.util.List;

public interface ApplicationDocumentCommandService {
    void createApplicationDocuments(List<String> imageUrls, Application application);
}
