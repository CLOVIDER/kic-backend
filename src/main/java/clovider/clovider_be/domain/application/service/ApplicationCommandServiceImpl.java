package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.document.service.ApplicationDocumentCommandService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationCommandServiceImpl implements ApplicationCommandService {


    private final ApplicationRepository applicationRepository;
    private final ApplicationDocumentCommandService applicationDocumentCommandService;
    private final LotteryCommandService lotteryCommandService;

    @Override
    public CustomResult applicationCreate(ApplicationRequest applicationRequest, Employee employee) {
        Application savedApplication = Application.builder()
                .employee(employee)
                .workYears(LocalDate.now().getYear() - employee.getJoinDt().getYear()) //현재 년도 - 입사 년도
                .isSingleParent(applicationRequest.getIsSingleParent())
                .childrenCnt(applicationRequest.getChildrenCnt())
                .isDisability(applicationRequest.getIsDisability())
                .isDualIncome(applicationRequest.getIsDualIncome())
                .isEmployeeCouple(applicationRequest.getIsEmployeeCouple())
                .isSibling(applicationRequest.getIsSibling())
                .isTemp(Save.APPLIED)
                .build();

        applicationRepository.save(savedApplication);

        applicationDocumentCommandService.createApplicationDocuments(applicationRequest.getFileUrls(), savedApplication);

        lotteryCommandService.insertLottery(applicationRequest.getChildrenRecruitList(), savedApplication.getId());

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationUpdate(Long Id, ApplicationRequest applicationRequest) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        savedApplication.update(applicationRequest);
        applicationDocumentCommandService.updateApplicationDocuments(applicationRequest.getFileUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationDelete(Long Id) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();
        lotteryCommandService.deleteLottery(Id);
        applicationRepository.delete(savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationTempSave(ApplicationRequest applicationRequest, Employee employee) {

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
                .isTemp(Save.TEMP)
                .build()
        );

        applicationDocumentCommandService.createApplicationDocuments(applicationRequest.getFileUrls(), savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

    @Override
    public CustomResult applicationAccept(Long Id, Map<DocumentType, Accept> acceptList) {
        Application savedApplication = applicationRepository.findById(Id).orElseThrow(
                () -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND)
        );
        ;
        List<Document> documents = savedApplication.getDocuments();

        for (Document document : documents) {

            DocumentType documentType = document.getDocumentType();
            Accept accept = acceptList.get(documentType);

            applicationDocumentCommandService.acceptDocument(document.getId(), accept);

        }

        // document가 모두 승인상태일때 신청서 승인처리
        savedApplication.changeAccept(documents.stream()
                .allMatch(document -> acceptList.getOrDefault(document.getDocumentType(), Accept.UNACCEPT).equals(Accept.ACCEPT))
                ? Accept.ACCEPT : Accept.UNACCEPT);

        applicationRepository.save(savedApplication);

        return CustomResult.toCustomResult(savedApplication.getId());
    }

}