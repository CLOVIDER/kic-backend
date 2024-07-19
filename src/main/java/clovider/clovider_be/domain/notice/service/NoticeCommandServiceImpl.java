package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    private static final String FIXED_ADMIN_USERNAME = "admin";

    public CustomResult createNotice(NoticeRequest request) {
        // 고정된 "admin"을 EmployeeRepository 에서 찾기
        Employee admin = employeeRepository.findByAccountId(FIXED_ADMIN_USERNAME)
                .orElseThrow(() -> new ApiException(ErrorStatus._ADMIN_NOT_FOUND));

        // + 로그인한 관리자 id를 SecurityContextHolder 에서 가져와 admin을 추가해서 생성해야함
        // 현재는 "admin"으로 고정
        Notice savedNotice = noticeRepository.save(Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .admin(admin)
                .build());

        noticeImageCommandService.createNoticeImages(request.getImageUrls(), savedNotice);

        return CustomResult.toCustomResult(savedNotice);
    }
}
