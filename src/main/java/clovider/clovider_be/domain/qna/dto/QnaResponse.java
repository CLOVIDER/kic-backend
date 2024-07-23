package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
