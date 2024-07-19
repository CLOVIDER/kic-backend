package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import org.springframework.stereotype.Service;

@Service
public interface NoticeQueryService {

    NoticeResponse getNotice(Long id);

    CustomPage<NoticeResponse> getAllNotices(int page, int size);
}
