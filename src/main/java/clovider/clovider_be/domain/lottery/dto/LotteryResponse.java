package clovider.clovider_be.domain.lottery.dto;

import clovider.clovider_be.domain.lottery.Lottery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
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

    @Schema(description = "어린이집별 신청현황 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcceptResult {

        @Schema(description = "어린이집 이름")
        private String kindergartenNm;
        @Schema(description = "승인 수")
        private Integer acceptCnt;
        @Schema(description = "미승인 수")
        private Integer unAcceptCnt;
        @Schema(description = "승인 대기 수")
        private Integer waitCnt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitResult {

        private String accountId;
        private String result;
    }

    public static List<RecruitResult> toRecruitResults(List<Lottery> lotteries) {

        return lotteries.stream()
                .map(lottery -> RecruitResult.builder()
                        .accountId(lottery.getApplication().getEmployee().getAccountId())
                        .result(lottery.getResult().getDescription())
                        .build())
                .toList();
    }
}
