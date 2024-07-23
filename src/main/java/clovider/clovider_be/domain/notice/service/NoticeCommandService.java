package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;

public interface NoticeCommandService {

    CustomResult createNotice(Employee employee, NoticeRequest noticeRequest);

    NoticeUpdateResponse updateNotice(Long noticeId, NoticeRequest noticeRequest);

    String deleteNotice(Long noticeId);
}
