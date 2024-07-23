package clovider.clovider_be.domain.kindergarten.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    public static KindergartenResponse toKindergertenResponse(Long kindergartenId,
            Long kindergartenImageId) {
        return KindergartenResponse.builder()
                .kindergartenId(kindergartenId)
                .kindergartenImageId(kindergartenImageId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}