package clovider.clovider_be.domain.noticeImage;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.notice.Notice;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class NoticeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

}
