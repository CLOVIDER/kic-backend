package clovider.clovider_be.domain.notice.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.Notice;
import java.util.List;
import lombok.Getter;

@Getter
public class NoticeRequest {

    private String title;
    private String content;
    private List<String> imageUrls;

    public static Notice toNotice(NoticeRequest noticeRequest, Employee admin) {
        return Notice.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .admin(admin)
                .build();
    }
}
