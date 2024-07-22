package clovider.clovider_be.domain.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplicationDto {

    private Integer workYears;
    private Character isSingleParent;
    private Integer childrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
}
