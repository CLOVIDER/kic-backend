package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.application.dto.ApplicationWriteDto;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.document.service.ApplicationDocumentCommandService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import jakarta.transaction.Transactional;
import java.net.Authenticator;
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
    public CustomResult applicationCreate(ApplicationWriteDto applicationWriteDto) {

        Employee employee = employeeQueryService.getEmployee(1L);

        Application savedApplication = applicationRepository.save(
                Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                .isSingleParent(applicationWriteDto.getIsSingleParent())
                .childrenCnt(applicationWriteDto.getChildrenCnt())
                .isDisability(applicationWriteDto.getIsDisability())
                .isDualIncome(applicationWriteDto.getIsDualIncome())
                .isEmployeeCouple(applicationWriteDto.getIsEmployeeCouple())
                .isSibling(applicationWriteDto.getIsSibling())
                .childName(applicationWriteDto.getChildName())
                .isTemp(applicationWriteDto.getIsTemp())
                .build()
        );

        applicationDocumentCommandService.createApplicationDocuments(applicationWriteDto.getImageUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationUpdate(Long Id, ApplicationUpdateDto applicationUpdateDto) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        savedApplication.update(applicationUpdateDto);

        applicationDocumentCommandService.createApplicationDocuments(applicationUpdateDto.getImageUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationDelete(Long Id) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        applicationRepository.delete(savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public void applicationTempSave(Application application) {

    }


}