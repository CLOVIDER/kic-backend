package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NowRecruitInfo {

        private LocalDateTime recruitStartDt;
        private LocalDateTime recruitEndDt;
        private Integer remainPeriod;
        private List<String> kindergartenList;
        private List<Double> rateList;
    }

    public static NowRecruitInfo toNowRecruitInfo(List<Recruit> recruits,
            List<CompetitionRate> competitionRates) {

        LocalDateTime startDt = recruits.get(0).getRecruitStartDt();
        LocalDateTime endDt = recruits.get(0).getRecruitEndDt();
        int dDay = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endDt);

        List<String> kindergartenNames = recruits.stream()
                .map(r -> r.getKindergarten().getKindergartenNm())
                .toList();

        List<Double> rates = competitionRates.stream()
                .map(CompetitionRate::getCompetitionRate)
                .map(rate -> Math.round(rate * 10) / 10.0)
                .toList();

        return NowRecruitInfo.builder()
                .recruitStartDt(startDt)
                .recruitEndDt(endDt)
                .remainPeriod(dDay)
                .kindergartenList(kindergartenNames)
                .rateList(rates)
                .build();
    }
}
