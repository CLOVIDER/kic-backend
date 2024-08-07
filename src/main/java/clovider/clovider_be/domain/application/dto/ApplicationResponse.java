package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.ChildInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Schema(description = "신청서 응답 DTO")
public class ApplicationResponse {

    private Long id; //신청서 id

    @JsonIgnore
    private Employee employee;

    private Integer workYears;
    private Character isSingleParent;
    private Integer ChildrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private Accept isAccept;
    private Save isTemp;

    private List<Document> documents;

    public static ApplicationResponse toEntity(Application application) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .employee(application.getEmployee())
                .workYears(application.getWorkYears())
                .isSingleParent(application.getIsSingleParent())
                .ChildrenCnt(application.getChildrenCnt())
                .isDisability(application.getIsDisability())
                .isDualIncome(application.getIsDualIncome())
                .isEmployeeCouple(application.getIsEmployeeCouple())
                .isSibling(application.getIsSibling())
                .isAccept(application.getIsAccept())
                .isTemp(application.getIsTemp())
                .documents(application.getDocuments())
                .build();
    }
}