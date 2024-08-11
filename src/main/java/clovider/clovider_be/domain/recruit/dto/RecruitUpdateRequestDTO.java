package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitUpdateRequestDTO {
    private Long kindergartenId;
    private AgeClass ageClass;
    private LocalDateTime recruitStartDt;
    private LocalDateTime recruitEndDt;
    private Integer recruitCnt;
    private LocalDateTime firstStartDt;
    private LocalDateTime firstEndDt;
    private LocalDateTime secondStartDt;
    private LocalDateTime secondEndDt;
    private RecruitResponse.RecruitWeightInfo recruitWeightInfo;
}