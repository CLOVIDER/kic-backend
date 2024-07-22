package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ApplicationWriteDto {

    //workYears는 기존 사용자 정보로 입력
    private Character isSingleParent;
    private Integer childrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private String childName;

//    public Application toEntity(Employee employee){
//        return Application.builder()
//                .isSingleParent(isSingleParent)
//                .childrenCnt(childrenCnt)
//                .isDisability(isDisability)
//                .isDualIncome(isDualIncome)
//                .isEmployeeCouple(isEmployeeCouple)
//                .isSibling(isSibling)
//                .childName(childName)
//                .employee(employee)
//                .build();
//    }
}