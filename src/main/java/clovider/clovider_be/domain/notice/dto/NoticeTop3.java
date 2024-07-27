package clovider.clovider_be.domain.notice.dto;

import clovider.clovider_be.domain.notice.Notice;
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
public class NoticeTop3 {

    private Long noticeId;
    private String title;
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
