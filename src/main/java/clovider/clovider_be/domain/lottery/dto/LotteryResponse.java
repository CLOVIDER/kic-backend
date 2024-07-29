package clovider.clovider_be.domain.lottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
}
