package clovider.clovider_be.domain.lottery.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LotteryIdAndChildNameDTO {
    private String childName;
    private Long lotteryId;
    private String className;
}
