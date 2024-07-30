package clovider.clovider_be.domain.noticeImage.dto;

import clovider.clovider_be.domain.noticeImage.NoticeImage;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeImageResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long noticeImageId;
    private String image;

    public static NoticeImageResponse toNoticeImageResponse(NoticeImage noticeImage) {
        return NoticeImageResponse.builder()
                .noticeImageId(noticeImage.getId())
                .image(noticeImage.getImage())
                .build();
    }
}
