package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.domain.noticeImage.NoticeImage;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCommandServiceImpl implements NoticeCommandService {

    private final NoticeRepository noticeRepository;
    private final NoticeQueryService noticeQueryService;
    private final EmployeeQueryService employeeQueryService;

    @Override
    public CustomResult createNotice(Employee employee, NoticeRequest request) {

        Notice savedNotice = noticeRepository.save(
                NoticeRequest.toNotice(request, employeeQueryService.getEmployee(employee.getId())));

        request.getImageUrls().forEach(url -> {
            NoticeImage image = NoticeImage.builder()
                    .image(url)
                    .notice(savedNotice)
                    .build();
            savedNotice.getImages().add(image);
        });

        return CustomResult.toCustomResult(savedNotice.getId());
    }

    @CacheEvict(value = "notices", key = "#noticeId")
    @Override
    public NoticeUpdateResponse updateNotice(Long noticeId, NoticeRequest request) {

        Notice foundNotice = noticeQueryService.findById(noticeId);

        foundNotice.updateNotice(request);

        return NoticeUpdateResponse.of(foundNotice.getId());
    }

    @CacheEvict(value = "notices", key = "#noticeId")
    @Override
    public String deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
        return "공지사항 삭제에 성공했습니다.";
    }
}
