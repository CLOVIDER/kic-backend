package clovider.clovider_be.domain.lottery.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LotteryResultsGroupedByChildDTO {
    private String childName;
    private List<LotteryResultByEmployeeDTO> lotteryResults;
}