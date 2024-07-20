package clovider.clovider_be.domain.qna.dto;

import lombok.Getter;

@Getter
public class QnaRequest {

    private String title;
    private String question;
    private boolean visibility;
}
