package clovider.clovider_be.domain.lottery.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightCalculationDTO {
    private Integer workYears;
    private Character singleParent;
    private Integer childrenCnt;
    private Character disability;
    private Character employeeCouple;
    private Character sibling;
    private Character dualIncome;

    public double calculateWeight() {
        double weight = 1.0;

        if (workYears != null && workYears > 0) weight += workYears * 1.0;
        if (singleParent != null && singleParent == '1') weight += 5.0;
        if (childrenCnt != null && childrenCnt >= 2) weight += 1.0;
        if (disability != null && disability == '1') weight += 4.0;
        if (dualIncome != null && dualIncome == '1') weight += 1.0;
        if (employeeCouple != null && employeeCouple == '1') weight += 5.0;
        if (sibling != null && sibling == '1') weight += 2.0;

        return weight;
    }
}

