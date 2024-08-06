package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.qna.Qna;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class QnaResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BaseQnaResponse {
        private Long qnaId;
        private String title;
        private String question;
        private Boolean isAnswerPresent;
        private Character isVisibility;
        private String writerName;
        private LocalDate createdAt;

        public static BaseQnaResponse fromQna(Qna qna) {
            return BaseQnaResponse.builder()
                    .qnaId(qna.getId())
                    .title(qna.getTitle())
                    .question(qna.getQuestion())
                    .isAnswerPresent(qna.getAnswer() != null)
                    .isVisibility(qna.getIsVisibility())
                    .writerName(qna.getEmployee().getNameKo())
                    .createdAt(qna.getCreatedAt().toLocalDate())
                    .build();
        }
    }

    // 답변을 포함하는 DTO
    @Getter
    @SuperBuilder
    public static class DetailedQnaResponse extends BaseQnaResponse {
        private final String answer;
        private final Boolean isAuthor;

        public static DetailedQnaResponse fromQna(Qna qna, Long employeeId) {
            return DetailedQnaResponse.builder()
                    .qnaId(qna.getId())
                    .title(qna.getTitle())
                    .question(qna.getQuestion())
                    .isAnswerPresent(qna.getAnswer() != null)
                    .isVisibility(qna.getIsVisibility())
                    .writerName(qna.getEmployee().getNameKo())
                    .createdAt(qna.getCreatedAt().toLocalDate())
                    .answer(qna.getAnswer())
                    .isAuthor(qna.getEmployee().getId().equals(employeeId))
                    .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QnaUpdateResponse {

        private Long qnaId;

        public static QnaUpdateResponse of(Long qnaId) {
            return QnaUpdateResponse.builder().qnaId(qnaId).build();
        }
    }

}
