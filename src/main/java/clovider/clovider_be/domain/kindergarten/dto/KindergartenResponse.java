package clovider.clovider_be.domain.kindergarten.dto;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KindergartenResponse {

    @Schema(description = "어린이집 정 등록 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenRegisterResponse {

        private Long kindergartenId;
        private Long kindergartenImageId;

        private String kindergartenNm;
        private String kindergartenAddr;
        private String kindergartenScale;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;

        private LocalDateTime createdAt;

        public static KindergartenRegisterResponse toKindergartenRegisterResponse(Kindergarten kindergarten,
                Long kindergartenImageId) {
            return KindergartenRegisterResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenImageId(kindergartenImageId)
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenNo(kindergarten.getKindergartenNo())
                    .kindergartenTime(kindergarten.getKindergartenTime())
                    .kindergartenInfo(kindergarten.getKindergartenInfo())
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Schema(description = "어린이집 정보 삭제 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergertenDeleteResponse {

        private Long kindergartenId;
        private Long kindergartenImageId;

        public static KindergertenDeleteResponse toKindergertenDeleteResponse(Long kindergartenId,
                Long kindergartenImageId) {
            return KindergertenDeleteResponse.builder()
                    .kindergartenId(kindergartenId)
                    .kindergartenImageId(kindergartenImageId)
                    .build();
        }
    }

    @Schema(description = "어린이집 정보 업데이트 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenUpdateResponse {

        private Long kindergartenId;
        private Long kindergartenImageId;

        private String kindergartenNm;
        private String kindergartenAddr;
        private String kindergartenScale;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;

        private LocalDateTime updatedAt;

        public static KindergartenUpdateResponse toKindergartenUpdateResponse(Kindergarten kindergarten,
                Long kindergartenImageId) {
            return KindergartenUpdateResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenNo(kindergarten.getKindergartenNo())
                    .kindergartenTime(kindergarten.getKindergartenTime())
                    .kindergartenInfo(kindergarten.getKindergartenInfo())
                    .kindergartenImageId(kindergartenImageId)
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Schema(description = "어린이집 정보 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenGetResponse {

        private Long kindergartenId;

        private String kindergartenNm;
        private String kindergartenAddr;
        private String kindergartenScale;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;

        private String kindergartenImageUrl;

        public static KindergartenGetResponse toKindergartenGetResponse(Kindergarten kindergarten, String kindergartenImageUrl) {
            return KindergartenGetResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenNo(kindergarten.getKindergartenNo())
                    .kindergartenTime(kindergarten.getKindergartenTime())
                    .kindergartenInfo(kindergarten.getKindergartenInfo())
                    .kindergartenImageUrl(kindergartenImageUrl)
                    .build();
        }
    }
}