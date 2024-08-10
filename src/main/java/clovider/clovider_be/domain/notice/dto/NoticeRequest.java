package clovider.clovider_be.domain.notice.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

@Getter
public class NoticeRequest {

    @Schema(description = "공지사항의 제목", example = "햇님 어린이집 07회차 모집 안내")
    @NotEmpty(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이내로 입력해 주세요.")
    private String title;

    @Schema(description = "공지사항의 내용", example = "햇님 어린이집 07회자 모집 안내입니다. ~~~~")
    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    @Schema(description = "공지사항에 첨부된 이미지 URL 목록", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
    @Size(max = 5, message = "이미지 URL 목록은 5개 이하의 URL만 포함할 수 있습니다.")
    private List<String> imageUrls;

    public static Notice toNotice(NoticeRequest noticeRequest, Employee admin) {
        return Notice.builder()
                .title(noticeRequest.getTitle())
                .content(noticeRequest.getContent())
                .admin(admin)
                .build();
    }
}
