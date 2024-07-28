package clovider.clovider_be.domain.notice.dto;


import clovider.clovider_be.domain.noticeImage.dto.NoticeImageResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeResponse {

    private Long noticeId;
    private String title;
    private String content;
    private int hits;
    private List<NoticeImageResponse> noticeImageList;
    private LocalDate createdAt;
}
