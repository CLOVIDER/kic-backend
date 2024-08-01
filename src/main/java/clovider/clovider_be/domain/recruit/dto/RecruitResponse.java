package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.enums.Period;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.global.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitResponse {

    @Schema(description = "현재 모집 중인 어린이집 정보 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NowRecruitInfo {

        @Schema(description = "모집 시작 기간", example = "2024-06-25T19:03:40")
        private String recruitStartDt;

        @Schema(description = "모집 마감 기간", example = "2024-07-25T19:03:40")
        private String recruitEndDt;

        @Schema(description = "1차 등록 기간", example = "2024-07-26T19:03:40")
        private String firstStartDt;

        @Schema(description = "1차 등록 마감 기간", example = "2024-07-31T19:03:40")
        private String firstEndDt;

        @Schema(description = "2차 등록 기간", example = "2024-08-01T19:03:40")
        private String secondStartDt;

        @Schema(description = "2차 등록 마감 기간", example = "2024-08-05T19:03:40")
        private String secondEndDt;

        @Schema(description = "모집 남은 기간", example = "7")
        private Integer remainPeriod;

        @Schema(description = "사내 어린이집 이름 및 나이별 모집 리스트", example = "새빛")
        private List<String> kindergartenClassList;

        @Schema(description = "각 사내 어린이집 경쟁률", example = "0.8")
        private List<Double> rateList;

        @Schema(description = "모집 상태", example = "모집중")
        private String recruitStatus;
    }

    public static NowRecruitInfo toNowRecruitInfo(List<Recruit> recruits,
            List<CompetitionRate> competitionRates) {

        String startDt = TimeUtil.formattedDateTime(recruits.get(0).getRecruitStartDt());
        String endDt = TimeUtil.formattedDateTime(recruits.get(0).getRecruitEndDt());
        String firstStartDt = TimeUtil.formattedDateTime(recruits.get(0).getFirstStartDt());
        String firstEndDt = TimeUtil.formattedDateTime(recruits.get(0).getFirstEndDt());
        String secondStartDt = TimeUtil.formattedDateTime(recruits.get(0).getSecondStartDt());
        String secondEndDt = TimeUtil.formattedDateTime(recruits.get(0).getSecondEndDt());
        int dDay = TimeUtil.formattedRemain(LocalDateTime.now(), recruits.get(0).getRecruitEndDt());

        String period = getPeriod(recruits.get(0), LocalDateTime.now());

        List<String> kindergartenClasses = recruits.stream()
                .map(r -> r.getKindergarten().getKindergartenNm() + ":" + r.getAgeClass()
                        .getDescription())
                .toList();

        List<Double> rates = competitionRates.stream()
                .map(CompetitionRate::getCompetitionRate)
                .map(rate -> Math.round(rate * 10) / 10.0)
                .toList();

        return NowRecruitInfo.builder()
                .recruitStartDt(startDt)
                .recruitEndDt(endDt)
                .firstStartDt(firstStartDt)
                .firstEndDt(firstEndDt)
                .secondStartDt(secondStartDt)
                .secondEndDt(secondEndDt)
                .remainPeriod(dDay)
                .kindergartenClassList(kindergartenClasses)
                .rateList(rates)
                .recruitStatus(period)
                .build();
    }

    public static NowRecruitInfo toNotRecruitInfo() {
        return NowRecruitInfo.builder()
                .recruitStatus(Period.NOT_RECRUIT.getDescription())
                .build();
    }

    private static String getPeriod(Recruit recruit, LocalDateTime now) {

        if (now.isBefore(recruit.getRecruitStartDt())) {
            return Period.SCHEDULED.getDescription();
        } else if (now.isAfter(recruit.getRecruitEndDt())) {
            return Period.NOT_RECRUIT.getDescription();
        } else if (now.isAfter(recruit.getFirstStartDt()) && now.isBefore(
                recruit.getFirstEndDt())) {
            return Period.FIRST_REGISTRY.getDescription();
        } else if (now.isAfter(recruit.getSecondStartDt()) && now.isBefore(
                recruit.getSecondEndDt())) {
            return Period.SECOND_REGISTRY.getDescription();
        } else if (now.isAfter(recruit.getRecruitStartDt()) && now.isBefore(
                recruit.getRecruitEndDt())) {
            return Period.ING.getDescription();
        } else {
            return Period.NOT_RECRUIT.getDescription();
        }
    }
}
