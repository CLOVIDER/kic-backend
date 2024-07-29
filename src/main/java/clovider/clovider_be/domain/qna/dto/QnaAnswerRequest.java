package clovider.clovider_be.domain.qna.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QnaAnswerRequest {
    @Schema(description = "답변 내용", example = "답변을 여기에 입력하세요.")
    @NotEmpty(message = "답변 내용은 필수입니다.")
    @Size(max = 500, message = "답변내용은 500자 이내로 입력해 주세요.")
    private String answer;
}
