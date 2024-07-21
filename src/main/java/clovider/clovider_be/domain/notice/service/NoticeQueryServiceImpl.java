package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeResponse getNotice(Long noticeId) {
        Notice foundNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));

        foundNotice.incrementHits();

        return NoticeResponse.toNoticeResponse(foundNotice);
    }

    @Override
    public CustomPage<NoticeResponse> getAllNotices(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notice> noticePage = noticeRepository.findAll(pageRequest);

        Page<NoticeResponse> noticeResponsePage = noticePage.map(NoticeResponse::toNoticeResponse);
        return new CustomPage<>(noticeResponsePage);
    }

}
