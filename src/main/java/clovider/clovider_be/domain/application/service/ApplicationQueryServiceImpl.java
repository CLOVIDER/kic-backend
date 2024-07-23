package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public ApplicationResponse applicationRead(Long Id){
        Application savedApplication = applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );

        return ApplicationResponse.toEntity(savedApplication);
    }

    @Override
    public void applicationPagination()
    {}
}