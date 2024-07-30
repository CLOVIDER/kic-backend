package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
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
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;

    @Override
    public ApplicationResponse applicationRead(Employee employee){
        Application savedApplication = applicationRepository.findFirstByEmployeeOrderByCreatedAtDesc(employee).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );

        return ApplicationResponse.toEntity(savedApplication);
    }


    @Override
    public ApplicationResponse applicationIdRead(Long Id){ //applicationId 기반 말고 유저 기반 정보를 가져오는 것이 필요
        Application savedApplication = applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );

        return ApplicationResponse.toEntity(savedApplication);
    }

    //TODO: 모집에 따른 구분 필요
    @Override
    public CustomPage<ApplicationResponse> applicationListRead(int page, int size)
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Application> applicationPage = applicationRepository.findAll(pageRequest);
        Page<ApplicationResponse> applicationListResponsePage = applicationPage.map(ApplicationResponse::toEntity);

        return new CustomPage<>(applicationListResponsePage);
    }

    @Override
    public Application getApplication(Long Id){
        return applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );
    }

    //
}