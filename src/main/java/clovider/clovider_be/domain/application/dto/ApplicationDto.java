package clovider.clovider_be.domain.application.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplicationDto {

    private Integer workYears;
    private Boolean singleParent;
    private Integer childrenCnt;
    private Boolean disability;
    private Boolean dualIncome;
    private Boolean employeeCouple;
    private Boolean sibling;
}
