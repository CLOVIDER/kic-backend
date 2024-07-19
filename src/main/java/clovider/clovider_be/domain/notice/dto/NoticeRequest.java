package clovider.clovider_be.domain.notice.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class NoticeRequest {

    private String title;
    private String content;
    private List<String> imageUrls;
}
