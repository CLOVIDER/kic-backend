package clovider.clovider_be.domain.qna.dto;

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
}
