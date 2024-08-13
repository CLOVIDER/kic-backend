package clovider.clovider_be.domain.lottery.dto;

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
    private Integer ageClass;

}
