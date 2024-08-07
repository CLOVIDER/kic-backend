package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;
    private final LotteryQueryService lotteryQueryService;

    @Override
    @Cacheable(value = "myApplication", key="#employee.id")
    public ApplicationResponse applicationRead(Employee employee) {
        Application savedApplication = applicationRepository.findFirstByEmployeeOrderByCreatedAtDesc(employee);

        if (savedApplication == null) {
            return ApplicationResponse.emptyEntity();
        }

        return ApplicationResponse.toEntity(savedApplication);
    }

    @Override
    public Page<ApplicationList> getNowApplications(List<Long> applicationIds, Pageable pageable,
            SearchVO searchVO) {

        return applicationRepository.getApplicationPage(applicationIds, pageable, searchVO);
    }

    @Override
    public ApplicationResponse applicationIdRead(
            Long Id) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );

        return ApplicationResponse.toEntity(savedApplication);
    }

    //TODO: 모집에 따른 구분 필요
    @Override
    public CustomPage<ApplicationResponse> applicationListRead(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Application> applicationPage = applicationRepository.findAll(pageRequest);
        Page<ApplicationResponse> applicationListResponsePage = applicationPage.map(
                ApplicationResponse::toEntity);

        return new CustomPage<>(applicationListResponsePage);
    }

    @Override
    public Application getApplication(Long Id){
        return applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );
    }

    @Override
    public Application getApplicationWithEmployee(Long applicationId) {
        return applicationRepository.findApplicationWithEmployee(applicationId);
    }

}