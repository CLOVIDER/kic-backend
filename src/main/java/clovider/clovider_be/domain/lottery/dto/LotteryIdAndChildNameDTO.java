package clovider.clovider_be.domain.lottery.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LotteryIdAndChildNameDTO {
    private String childName;
    private List<Long> lotteryIds;
}
