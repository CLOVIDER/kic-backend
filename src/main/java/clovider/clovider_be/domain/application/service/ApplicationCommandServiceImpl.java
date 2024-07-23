package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.document.service.ApplicationDocumentCommandService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationCommandServiceImpl implements ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationDocumentCommandService applicationDocumentCommandService;
    private final EmployeeQueryService employeeQueryService;

    @Override
    public CustomResult applicationCreate(ApplicationRequest applicationRequest) {

        // TODO: 토큰으로 유저정보 사용하기
        Employee employee = employeeQueryService.getEmployee(1L);

        Application savedApplication = applicationRepository.save(
                Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                .isSingleParent(applicationRequest.getIsSingleParent())
                .childrenCnt(applicationRequest.getChildrenCnt())
                .isDisability(applicationRequest.getIsDisability())
                .isDualIncome(applicationRequest.getIsDualIncome())
                .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                .isSibling(applicationRequest.getIsSibling())
                .childName(applicationRequest.getChildName())
                .isTemp('0')
                .build()
        );

        applicationDocumentCommandService.createApplicationDocuments(applicationRequest.getImageUrls(), savedApplication);

        //TODO: 추첨 테이블에 들어가는 로직 cnrk해야함

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationUpdate(Long Id, ApplicationRequest applicationRequest) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        savedApplication.update(applicationRequest);

        applicationDocumentCommandService.createApplicationDocuments(applicationRequest.getImageUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationDelete(Long Id) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        applicationRepository.delete(savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationTempSave(ApplicationRequest applicationRequest) {
        // TODO: 토큰으로 유저정보 사용하기
        Employee employee = employeeQueryService.getEmployee(1L);

        Application savedApplication = applicationRepository.save(
                Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                .isSingleParent(applicationRequest.getIsSingleParent())
                .childrenCnt(applicationRequest.getChildrenCnt())
                .isDisability(applicationRequest.getIsDisability())
                .isDualIncome(applicationRequest.getIsDualIncome())
                .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                .isSibling(applicationRequest.getIsSibling())
                .childName(applicationRequest.getChildName())
                .isTemp('1')
                .build()
        );

        applicationDocumentCommandService.createApplicationDocuments(applicationRequest.getImageUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }


}