package clovider.clovider_be.domain.admin.dto;

import static clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;

import clovider.clovider_be.domain.admin.dto.AdminResponse.KindergartenClassInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequest {

    @Schema(description = "모집 생성 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitCreationRequest{
        @Schema(description = "어린이집 및 분반 정보 리스트")
        private List<KindergartenClassInfo> kindergartenClassInfoList;

        @Schema(description = "모집 기간 상세 및 가중치 설정 정보")
        private RecruitDateAndWeightInfo recruitDateAndWeightInfo;
    }

}
