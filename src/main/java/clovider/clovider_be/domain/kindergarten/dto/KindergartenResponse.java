package clovider.clovider_be.domain.kindergarten.dto;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "어린이집 응답 DTO")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KindergartenResponse {

    private Long kindergartenId;
    private Long kindergartenImageId;

    private String kindergartenNm;
    private String kindergartenAddr;
    private String kindergartenScale;
    private String kindergartenNo;
    private String kindergartenTime;
    private String kindergartenInfo;
    private List<String> kindergartenImageUrls;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static KindergartenResponse toKindergertenDeleteResponse(Long kindergartenId,
            Long kindergartenImageId) {
        return KindergartenResponse.builder()
                .kindergartenId(kindergartenId)
                .kindergartenImageId(kindergartenImageId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static KindergartenResponse toKindergertenUpdateResponse(Kindergarten kindergarten,
            Long kindergartenImageId) {
        return KindergartenResponse.builder()
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

    public static KindergartenResponse toKindergartenGetResponse(Kindergarten kindergarten,
            List<String> kindergartenImageUrls) {
        return KindergartenResponse.builder()
                .kindergartenId(kindergarten.getId())
                .kindergartenNm(kindergarten.getKindergartenNm())
                .kindergartenAddr(kindergarten.getKindergartenAddr())
                .kindergartenScale(kindergarten.getKindergartenScale())
                .kindergartenNo(kindergarten.getKindergartenNo())
                .kindergartenTime(kindergarten.getKindergartenTime())
                .kindergartenInfo(kindergarten.getKindergartenInfo())
                .kindergartenImageUrls(kindergartenImageUrls)
                .updatedAt(LocalDateTime.now())
                .build();
    }


}