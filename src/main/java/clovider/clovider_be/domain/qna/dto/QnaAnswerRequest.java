package clovider.clovider_be.domain.qna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class QnaAnswerRequest {
    @Schema(description = "답변 내용", example = "답변을 여기에 입력하세요.")
    private String answer;
}
