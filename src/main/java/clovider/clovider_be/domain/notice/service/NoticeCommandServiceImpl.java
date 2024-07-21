package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.domain.noticeImage.service.NoticeImageCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final NoticeImageCommandService noticeImageCommandService;
    private final NoticeQueryService noticeQueryService;
    private final EmployeeQueryService employeeQueryService;

    public CustomResult createNotice(NoticeRequest request) {

        // 로그인한 관리자 id를 SecurityContextHolder 에서 가져와 admin을 추가해서 생성 요망
        // 현재는 1번 데이터로 고정
        Notice savedNotice = noticeRepository.save(
                NoticeRequest.toNotice(request, employeeQueryService.findById(1L)));

        noticeImageCommandService.createNoticeImages(request.getImageUrls(), savedNotice);

        return CustomResult.toCustomResult(savedNotice.getId());
    }

    public NoticeUpdateResponse updateNotice(Long noticeId, NoticeRequest request) {

        Notice foundNotice = noticeQueryService.findById(noticeId);

        foundNotice.updateNotice(request);

        return NoticeUpdateResponse.of(foundNotice.getId());
    }

    public String deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
        return "공지사항 삭제에 성공했습니다.";
    }
}
