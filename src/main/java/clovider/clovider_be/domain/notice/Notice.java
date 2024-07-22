package clovider.clovider_be.domain.notice;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "notice_tb")
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeImage> images = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int hits = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Employee admin;

    public void updateNotice(NoticeRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();

        // 기존 이미지를 삭제하고 새로운 이미지를 추가
        updateImages(request.getImageUrls());
    }

    private void updateImages(List<String> imageUrls) {
        // 기존 이미지를 제거
        this.images.clear();

        // 새로운 이미지를 추가
        imageUrls.forEach(url -> {
            NoticeImage image = NoticeImage.builder()
                    .image(url)
                    .notice(this)
                    .build();
            this.images.add(image);
        });
    }

    public void incrementHits() {
        this.hits += 1;
    }
}
