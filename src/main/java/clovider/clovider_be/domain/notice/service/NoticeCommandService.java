package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;

public interface NoticeCommandService {

    CustomResult createNotice(NoticeRequest noticeRequest);

    CustomResult updateNotice(Long noticeId, NoticeRequest noticeRequest);

    CustomResult deleteNotice(Long noticeId);
}
