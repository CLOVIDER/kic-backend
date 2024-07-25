package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.Qna;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class QnaRequest {

    @Schema(description = "질문 제목", example = "시스템 오류 문의", required = true, maxLength = 100)
    private String title;

    @Schema(description = "질문 내용", example = "시스템 오류가 발생했습니다. 도움을 요청합니다.", maxLength = 500)
    private String question;

    @Schema(description = "공지사항의 공개 여부", example = "1", allowableValues = {"0", "1"})
    private Character isVisibility;

    public static Qna toQna(QnaRequest qnaRequest, Employee employee) {
        return Qna.builder()
                .title(qnaRequest.getTitle())
                .question(qnaRequest.getQuestion())
                .isVisibility(qnaRequest.getIsVisibility())
                .employee(employee).build();
    }
}
