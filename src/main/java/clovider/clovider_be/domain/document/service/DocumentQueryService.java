package clovider.clovider_be.domain.document.service;

import clovider.clovider_be.domain.document.dto.DocumentResponse.DocumentInfo;
import java.util.List;

public interface DocumentQueryService {
    List<DocumentInfo> getDocumentInfos(Long applicationId);
}
