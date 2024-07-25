package clovider.clovider_be.domain.qna;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@DynamicInsert
@Table(name = "qna_tb")
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(length = 500)
    private String answer;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isVisibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Employee admin;

    public void updateQna(QnaRequest qnaRequest) {
        this.title = qnaRequest.getTitle();
        this.question = qnaRequest.getQuestion();
        this.isVisibility = qnaRequest.getIsVisibility();
    }

    public static QnaResponse toQnaResponse(Qna qna) {
        return QnaResponse.builder()
                .qnaId(qna.getId())
                .title(qna.getTitle())
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .isVisibility(qna.getIsVisibility())
                .writerName(qna.employee.getNameKo())
                .createdAt(qna.getCreatedAt().toLocalDate())
                .build();
    }
}
