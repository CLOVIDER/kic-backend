package clovider.clovider_be.domain.document.dto;

import clovider.clovider_be.domain.document.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DocumentResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DocumentInfo {
        private String type;
        private String documentUrl;
    }

    public static DocumentInfo toDocumentInfo (Document document){
        return DocumentInfo.builder()
                .type(document.getDocumentType().getDescription())
                .documentUrl(document.getImage())
                .build();
    }
}
