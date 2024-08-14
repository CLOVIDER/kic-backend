package clovider.clovider_be.domain.recruit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    public static class RecruitClassInfo {

        @Schema(description = "분반 이름", example = "0세")
        private Integer ageClass;

        @Schema(description = "모집인원", example = "20")
        private Integer recruitCnt;
    }

}
