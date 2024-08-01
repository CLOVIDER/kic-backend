package clovider.clovider_be.domain.noticeImage.service;

import clovider.clovider_be.domain.notice.Notice;
import java.util.List;

public interface NoticeImageCommandService {

    void createNoticeImages(List<String> imageUrls, Notice notice);
}
