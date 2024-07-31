package clovider.clovider_be.domain.noticeImage.dto;

import clovider.clovider_be.domain.noticeImage.NoticeImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeImageResponse {

    private Long noticeImageId;
    private String image;

    public static NoticeImageResponse toNoticeImageResponse(NoticeImage noticeImage) {
        return NoticeImageResponse.builder()
                .noticeImageId(noticeImage.getId())
                .image(noticeImage.getImage())
                .build();
    }
}
