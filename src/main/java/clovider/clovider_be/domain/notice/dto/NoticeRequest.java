package clovider.clovider_be.domain.notice.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
public class NoticeRequest {

    @Schema(description = "공지사항의 제목", example = "햇님 어린이집 07회차 모집 안내")
    private String title;
    @Schema(description = "공지사항의 내용", example = "햇님 어린이집 07회자 모집 안내입니다. ~~~~")
    private String content;
    @Schema(description = "공지사항에 첨부된 이미지 URL 목록", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    private List<String> imageUrls;

    public static Notice toNotice(NoticeRequest noticeRequest, Employee admin) {
        return Notice.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .admin(admin)
                .build();
    }
}
