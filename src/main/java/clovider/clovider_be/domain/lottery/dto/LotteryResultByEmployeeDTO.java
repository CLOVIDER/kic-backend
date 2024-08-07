package clovider.clovider_be.domain.lottery.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LotteryResultByEmployeeDTO {

    private Long applicationId;
    private Long recruitId;
    private String childName;
    private String result;
    private Integer waitingNumber;
    private String kindergartenName;
    private AgeClass ageClass;


}
