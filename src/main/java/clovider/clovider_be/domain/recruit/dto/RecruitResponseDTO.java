package clovider.clovider_be.domain.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RecruitResponseDTO {
    private boolean isSuccess;
    private String code;
    private String message;
    private Result result;

    @Data
    @AllArgsConstructor
    public static class Result {
        private List<KindergartenClassInfo> kindergartenClassInfoList;
        private RecruitDateAndWeightInfo recruitDateAndWeightInfo;
        private boolean isCreated;
    }

    @Data
    @AllArgsConstructor
    public static class KindergartenClassInfo {
        private String kindergartenName;
        private List<ClassInfo> classInfoList;
    }

    @Data
    @AllArgsConstructor
    public static class ClassInfo {
        private String ageClass; // Example: "0~2세"
        private int recruitCnt;
    }

    @Data
    @AllArgsConstructor
    public static class RecruitDateAndWeightInfo {
        private RecruitDateInfo recruitDateInfo;
        private RecruitWeightInfo recruitWeightInfo;
    }

    @Data
    @AllArgsConstructor
    public static class RecruitDateInfo {
        private LocalDateTime recruitStartDt;
        private LocalDateTime recruitEndDt;
        private LocalDateTime firstStartDt;
        private LocalDateTime firstEndDt;
        private LocalDateTime secondStartDt;
        private LocalDateTime secondEndDt;
    }

    @Data
    @AllArgsConstructor
    public static class RecruitWeightInfo {
        private char workYearsUsage;
        private char isSingleParentUsage;
        private char childrenCntUsage;
        private char isDisabilityUsage;
        private char isDualIncomeUsage;
        private char isEmployeeCoupleUsage;
        private char isSiblingUsage;
    }
}
