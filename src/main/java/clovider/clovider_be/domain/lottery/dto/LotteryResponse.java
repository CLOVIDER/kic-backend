package clovider.clovider_be.domain.lottery.dto;

import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.recruit.Recruit;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitInfo {
        private String kindergartenNm;
        private Integer ageClass;
    }

    public static RecruitInfo toRecruitInfo(Recruit recruit) {

        return RecruitInfo.builder()
                .kindergartenNm(recruit.getKindergarten().getKindergartenNm())
                .ageClass(recruit.getAgeClass())
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChildInfo {
        private String childName;
        private List<RecruitInfo> recruitInfos;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LotteryHistory {
        private Long lotteryId;
        private String childName;
        private String kindergartenName;
        private Integer ageClass;
        private String result;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime applicationDate;
        private String competition;
    }


}
