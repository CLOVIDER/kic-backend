package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.domain.noticeImage.service.NoticeImageCommandService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final NoticeImageCommandService noticeImageCommandService;
    private final EmployeeQueryService employeeQueryService;

    public CustomResult createNotice(NoticeRequest request) {

        // 로그인한 관리자 id를 SecurityContextHolder 에서 가져와 admin을 추가해서 생성 요망
        // 현재는 1번 데이터로 고정
        Notice savedNotice = noticeRepository.save(Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .admin(employeeQueryService.findById(1L))
                .build());

        noticeImageCommandService.createNoticeImages(request.getImageUrls(), savedNotice);

        return CustomResult.toCustomResult(savedNotice.getId());
    }

    public CustomResult updateNotice(Long noticeId, NoticeRequest request) {

        Notice foundNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));

        foundNotice.updateNotice(request);

        return CustomResult.toCustomResult(foundNotice.getId());
    }

    public CustomResult deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
        return CustomResult.toCustomResult(noticeId);
    }
}
