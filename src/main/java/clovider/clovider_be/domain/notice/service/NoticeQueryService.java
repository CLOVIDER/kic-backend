package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface NoticeQueryService {

    Notice findById(Long id);

    NoticeResponse getNotice(Long id);

    CustomPage<NoticeResponse> getAllNotices(int page, int size);

    List<NoticeTop3> getTop3Notices();
}
