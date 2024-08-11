package clovider.clovider_be.domain.qna;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qnaImage.QnaImage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Builder.Default
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaImage> images = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
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

    public void updateQna(QnaCreateRequest qnaCreateRequest) {
        this.title = qnaCreateRequest.getTitle();
        this.question = qnaCreateRequest.getQuestion();
        this.isVisibility = qnaCreateRequest.getIsVisibility();
        updateImages(qnaCreateRequest.getImageUrls());
    }

    private void updateImages(List<String> imageUrls) {
        // 기존 이미지를 제거
        this.images.clear();

        // 새로운 이미지를 추가
        imageUrls.forEach(url -> {
            QnaImage image = QnaImage.builder()
                    .image(url)
                    .qna(this)
                    .build();
            this.images.add(image);
        });
    }

    public void updateAnswer(String answer, Employee admin) {
        this.answer = answer;
        this.admin = admin;
    }
}
