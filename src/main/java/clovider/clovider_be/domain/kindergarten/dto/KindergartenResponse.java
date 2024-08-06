package clovider.clovider_be.domain.kindergarten.dto;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KindergartenResponse {

    @Schema(description = "어린이집 정보 등록 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenRegisterResponse {

        private Long kindergartenId;
        private List<Long> kindergartenImageIds;
        private String kindergartenNm;
        private String kindergartenAddr;
        private Integer kindergartenScale;
        private Integer kindergartenCapacity;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;
        private LocalDateTime createdAt;

        public static KindergartenRegisterResponse toKindergartenRegisterResponse(Kindergarten kindergarten, List<Long> kindergartenImageIds) {
            return KindergartenRegisterResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenImageIds(kindergartenImageIds)
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenCapacity(kindergarten.getKindergartenCapacity())
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
    public static class KindergartenDeleteResponse {

        private Long kindergartenId;
        private List<Long> relatedRecruit;

        public static KindergartenDeleteResponse toKindergartenDeleteResponse(Long kindergartenId,
                List<Long> relatedRecruit) {
            return KindergartenDeleteResponse.builder()
                    .kindergartenId(kindergartenId)
                    .relatedRecruit(relatedRecruit)
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
        private List<Long> kindergartenImageIds;
        private String kindergartenNm;
        private String kindergartenAddr;
        private Integer kindergartenScale;
        private Integer kindergartenCapacity;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;
        private LocalDateTime updatedAt;

        public static KindergartenUpdateResponse toKindergartenUpdateResponse(
                Kindergarten kindergarten,
                List<Long> kindergartenImageIds) {
            return KindergartenUpdateResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenCapacity(kindergarten.getKindergartenCapacity())
                    .kindergartenNo(kindergarten.getKindergartenNo())
                    .kindergartenTime(kindergarten.getKindergartenTime())
                    .kindergartenInfo(kindergarten.getKindergartenInfo())
                    .kindergartenImageIds(kindergartenImageIds)
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
        private Integer kindergartenScale;
        private Integer kindergartenCapacity;
        private String kindergartenNo;
        private String kindergartenTime;
        private String kindergartenInfo;

        private List<String> kindergartenImageUrls;

        public static KindergartenGetResponse toKindergartenGetResponse(Kindergarten kindergarten,
                List<String> kindergartenImageUrls) {
            return KindergartenGetResponse.builder()
                    .kindergartenId(kindergarten.getId())
                    .kindergartenNm(kindergarten.getKindergartenNm())
                    .kindergartenAddr(kindergarten.getKindergartenAddr())
                    .kindergartenScale(kindergarten.getKindergartenScale())
                    .kindergartenCapacity(kindergarten.getKindergartenCapacity())
                    .kindergartenNo(kindergarten.getKindergartenNo())
                    .kindergartenTime(kindergarten.getKindergartenTime())
                    .kindergartenInfo(kindergarten.getKindergartenInfo())
                    .kindergartenImageUrls(kindergartenImageUrls)
                    .build();
        }
    }
}