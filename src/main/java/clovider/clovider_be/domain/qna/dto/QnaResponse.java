package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.qna.Qna;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaResponse {
    private Long qnaId;
    private String title;
    private String question;
    private String answer;
    private Character isVisibility;
    private String writerName;
    private LocalDate createdAt;

    public static QnaResponse toQnaResponse(Qna qna) {
        return QnaResponse.builder()
                .qnaId(qna.getId())
                .title(qna.getTitle())
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .isVisibility(qna.getIsVisibility())
                .writerName(qna.getEmployee().getNameKo())
                .createdAt(qna.getCreatedAt().toLocalDate())
                .build();
    }
}
