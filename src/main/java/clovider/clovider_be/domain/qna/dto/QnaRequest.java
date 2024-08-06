package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.Qna;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QnaRequest {

    @Schema(description = "질문 제목", example = "시스템 오류 문의", maxLength = 100)
    @NotEmpty(message = "질문 제목은 필수입니다.")
    @Size(max = 100, message = "질문 제목은 100자 이내로 입력해 주세요.")
    private String title;

    @Schema(description = "질문 내용", example = "시스템 오류가 발생했습니다. 도움을 요청합니다.", maxLength = 500)
    @NotEmpty(message = "질문 내용은 필수입니다.")
    @Size(max = 500, message = "제목은 500자 이내로 입력해 주세요.")
    private String question;

    @Schema(description = "공지사항의 공개 여부", example = "1", allowableValues = {"0", "1"})
    @NotNull(message = "공개여부는 필수입니다.")
    private Character isVisibility;

    public static Qna toQna(QnaRequest qnaRequest, Employee employee) {
        return Qna.builder()
                .title(qnaRequest.getTitle())
                .question(qnaRequest.getQuestion())
                .isVisibility(qnaRequest.getIsVisibility())
                .employee(employee).build();
    }
}
