package clovider.clovider_be.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeUpdateResponse {

    private Long noticeId;

    public static NoticeUpdateResponse of(Long noticeId) {
        return NoticeUpdateResponse.builder().noticeId(noticeId).build();
    }
}
