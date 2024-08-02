package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecruitCreateRequestDTO {
    private LocalDateTime recruitStartDt;
    private LocalDateTime recruitEndDt;
    private Integer recruitCnt;
    private AgeClass ageClass;
    private LocalDateTime firstStartDt;
    private LocalDateTime firstEndDt;
    private LocalDateTime secondStartDt;
    private LocalDateTime secondEndDt;
    private Long kindergartenId;
    private Integer workYearsUsage;
    private Character isSingleParentUsage;
    private Character childrenCntUsage;
    private Character isDisabilityUsage;
    private Character isDualIncomeUsage;
    private Character isEmployeeCoupleUsage;
    private Character isSiblingUsage;
}
