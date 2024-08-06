package clovider.clovider_be.domain.lottery.dto;

import clovider.clovider_be.domain.recruit.Recruit;

public class WeightCalculationDTO {
    private Integer workYears;
    private Character isSingleParent;
    private Integer childrenCnt;
    private Character isDisability;
    private Character isEmployeeCouple;
    private Character isSibling;
    private Character isDualIncome;

    private Character workYearsUsage;
    private Character isSingleParentUsage;
    private Character childrenCntUsage;
    private Character isDisabilityUsage;
    private Character isEmployeeCoupleUsage;
    private Character isSiblingUsage;
    private Character isDualIncomeUsage;

    public WeightCalculationDTO(
            Integer workYears,
            Character isSingleParent,
            Integer childrenCnt,
            Character isDisability,
            Character isEmployeeCouple,
            Character isSibling,
            Character isDualIncome,
            Recruit recruit
    ) {
        this.workYears = workYears;
        this.isSingleParent = isSingleParent;
        this.childrenCnt = childrenCnt;
        this.isDisability = isDisability;
        this.isEmployeeCouple = isEmployeeCouple;
        this.isSibling = isSibling;
        this.isDualIncome = isDualIncome;

        this.workYearsUsage = recruit.getWorkYearsUsage();
        this.isSingleParentUsage = recruit.getIsSingleParentUsage();
        this.childrenCntUsage = recruit.getChildrenCntUsage();
        this.isDisabilityUsage = recruit.getIsDisabilityUsage();
        this.isEmployeeCoupleUsage = recruit.getIsEmployeeCoupleUsage();
        this.isSiblingUsage = recruit.getIsSiblingUsage();
        this.isDualIncomeUsage = recruit.getIsDualIncomeUsage();
    }

    // 가중치 계산 로직
    public double calculateWeight() {
        double weight = 1.0;

        if (workYearsUsage == '1' && workYears != null) {
            weight += workYears * 0.1;
        }
        if (isSingleParentUsage == '1' && isSingleParent != null && isSingleParent == '1') {
            weight += 0.5;
        }
        if (childrenCntUsage == '1' && childrenCnt != null) {
            weight += childrenCnt * 0.2;
        }
        if (isDisabilityUsage == '1' && isDisability != null && isDisability == '1') {
            weight += 0.5;
        }
        if (isEmployeeCoupleUsage == '1' && isEmployeeCouple != null && isEmployeeCouple == '1') {
            weight += 0.3;
        }
        if (isSiblingUsage == '1' && isSibling != null && isSibling == '1') {
            weight += 0.2;
        }
        if (isDualIncomeUsage == '1' && isDualIncome != null && isDualIncome == '1') {
            weight += 0.3;
        }

        return weight;
    }
}
