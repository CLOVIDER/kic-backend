package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitCreateRequestDTO {
    private List<KindergartenRecruitRequest> kindergartens;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenRecruitRequest {
        private Long kindergartenId;
        private List<RecruitClassCreateRequestDTO> classes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitClassCreateRequestDTO {
        private Integer ageClass;
        private LocalDateTime recruitStartDt;
        private LocalDateTime recruitEndDt;
        private Integer recruitCnt;
        private LocalDateTime firstStartDt;
        private LocalDateTime firstEndDt;
        private LocalDateTime secondStartDt;
        private LocalDateTime secondEndDt;
        private Character workYearsUsage;
        private Character isSingleParentUsage;
        private Character childrenCntUsage;
        private Character isDisabilityUsage;
        private Character isDualIncomeUsage;
        private Character isEmployeeCoupleUsage;
        private Character isSiblingUsage;
    }

}
