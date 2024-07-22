package clovider.clovider_be.domain.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplicationDto {

    private Integer workYears;
    private Boolean isSingleParent;
    private Integer childrenCnt;
    private Boolean isDisability;
    private Boolean isDualIncome;
    private Boolean isEmployeeCouple;
    private Boolean isSibling;
}
