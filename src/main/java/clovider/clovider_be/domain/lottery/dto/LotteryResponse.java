package clovider.clovider_be.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LotteryResponse {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompetitionRate {

        private Long recruitId;
        private Double competitionRate;

    }
}
