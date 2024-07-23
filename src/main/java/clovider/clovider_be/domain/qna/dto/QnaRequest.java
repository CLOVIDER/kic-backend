package clovider.clovider_be.domain.qna.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.Qna;
import lombok.Getter;

@Getter
public class QnaRequest {

    private String title;
    private String question;
    private Character isVisibility;

    public static Qna toQna(QnaRequest qnaRequest, Employee employee) {
        return Qna.builder()
                .title(qnaRequest.getTitle())
                .question(qnaRequest.getQuestion())
                .isVisibility(qnaRequest.getIsVisibility())
                .employee(employee).build();
    }
}
