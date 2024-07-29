package clovider.clovider_be.domain.notice.dto;

import clovider.clovider_be.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "최신 3개의 공지사항 DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeTop3 {

    @Schema(description = "공지사항 ID")
    private Long noticeId;
    @Schema(description = "공지사항 제목")
    private String title;
    @Schema(description = "공지사항 생성시간")
    private LocalDate createdAt;

    public static NoticeTop3 toNoticeTops(Notice notice) {

        return NoticeTop3.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt().toLocalDate())
                .build();
    }

    public static List<NoticeTop3> from(List<Notice> notices) {

        return notices.stream()
                .map(NoticeTop3::toNoticeTops)
                .toList();
    }
}
