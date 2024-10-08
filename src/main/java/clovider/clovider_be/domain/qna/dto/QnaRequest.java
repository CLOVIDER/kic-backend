package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.Qna;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

@Getter
public class QnaRequest {

    @Getter
    public static class QnaCreateRequest {
        @Schema(description = "질문 제목", example = "시스템 오류 문의", maxLength = 100)
        @NotEmpty(message = "질문 제목은 필수입니다.")
        @Size(max = 100, message = "질문 제목은 100자 이내로 입력해 주세요.")
        private String title;

        @Schema(description = "질문 내용", example = "시스템 오류가 발생했습니다. 도움을 요청합니다.")
        @NotEmpty(message = "질문 내용은 필수입니다.")
        private String question;

        @Schema(description = "공지사항의 공개 여부", example = "1", allowableValues = {"0", "1"})
        @NotNull(message = "공개여부는 필수입니다.")
        private Character isVisibility;

        @Schema(description = "QNA에 첨부된 이미지 URL 목록", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
        @Size(max = 5, message = "이미지 URL 목록은 5개 이하의 URL만 포함할 수 있습니다.")
        private List<String> imageUrls;

        public static Qna toQna(QnaCreateRequest qnaCreateRequest, Employee employee) {
            return Qna.builder()
                    .title(qnaCreateRequest.getTitle())
                    .question(qnaCreateRequest.getQuestion())
                    .isVisibility(qnaCreateRequest.getIsVisibility())
                    .employee(employee).build();
        }
    }

    @Getter
    public static class QnaAnswerRequest {
        @Schema(description = "답변 내용", example = "답변을 여기에 입력하세요.")
        @NotEmpty(message = "답변 내용은 필수입니다.")
        private String answer;
    }


}
