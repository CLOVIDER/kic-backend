package clovider.clovider_be.domain.notice.dto;


import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.noticeImage.dto.NoticeImageResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    public static NoticeResponse toNoticeResponse(Notice notice) {
        List<NoticeImageResponse> noticeImageResponseList = notice.getImages().stream()
                .map(NoticeImageResponse::toNoticeImageResponse)
                .toList();

        return NoticeResponse.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .hits(notice.getHits())
                .noticeImageList(noticeImageResponseList)
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
