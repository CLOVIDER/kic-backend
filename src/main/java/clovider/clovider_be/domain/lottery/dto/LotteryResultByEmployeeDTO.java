package clovider.clovider_be.domain.lottery.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import lombok.Data;

@Data
public class LotteryResultByEmployeeDTO {

    private Long applicationId;
    private Long recruitId;
    private String childName;
    private String result;
    private Integer waitingNumber;
    private String kindergartenName;
    private AgeClass ageClass;


    public void setKindergartenNm(String kindergartenNm) {
        this.kindergartenName = kindergartenNm;
    }

    public void setAgeClass(AgeClass ageClass) {
        this.ageClass = ageClass;
    }

    public void setRecruitId(Long recruitId) {
        this.recruitId = recruitId;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setWaitingNumber(Integer waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
}
