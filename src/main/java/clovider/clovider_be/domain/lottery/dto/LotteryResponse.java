package clovider.clovider_be.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TotalApplication {

        private String kindergartenNm;
        private Integer totalCnt;
    }

    public static TotalApplication toTotalApplication(String name, Integer count) {

        return TotalApplication.builder()
                .kindergartenNm(name)
                .totalCnt(count)
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcceptResult {

        private String kindergartenNm;
        private Integer acceptCnt;
        private Integer unAcceptCnt;
        private Integer waitCnt;
    }
}
