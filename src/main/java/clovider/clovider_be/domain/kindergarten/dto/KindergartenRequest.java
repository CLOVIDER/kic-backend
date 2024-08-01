package clovider.clovider_be.domain.kindergarten.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class KindergartenRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "새로운 어린이집 상세정보를 등록하기 위한 요청 DTO")
    public static class KindergartenRegisterRequest{
        @Schema(description = "어린이집 이름", example = "샛별 어린이집", required = true)
        @NotNull(message = "어린이집 이름은 필수 항목입니다.")
        private String kindergartenNm;

        @Schema(description = "어린이집 주소", example = "경기도 성남시", required = true)
        @NotNull(message = "어린이집 주소는 필수 항목입니다.")
        private String kindergartenAddr;

        @Schema(description = "어린이집 규모(정원, 크기)", example = "정원: 150명, 크기: 10m^2/명", required = true)
        @NotNull(message = "어린이집 규모는 필수 항목입니다.")
        private String kindergartenScale;

        @Schema(description = "어린이집 전화번호", example = "031-1234-5678", required = true)
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. 형식: 000-0000-0000")
        @NotNull(message = "어린이집 전화번호는 필수 항목입니다.")
        private String kindergartenNo;

        @Schema(description = "어린이집 운영시간", example = "7:00 - 22:00", required = true)
        @NotNull(message = "어린이집 운영시간은 필수 항목입니다.")
        private String kindergartenTime;

        @Schema(description = "어린이집 기타 정보", example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.", required = true)
        @NotNull(message = "어린이집 기타 정보는 필수 항목입니다.")
        private String kindergartenInfo;

        @Schema(description = "어린이집 이미지 url", example = "path/file.png", required = true)
        @NotNull(message = "어린이집 이미지 url은 필수 항목입니다.")
        private String kindergartenImage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "새로운 어린이집 상세정보를 등록하기 위한 요청 DTO")
    public static class KindergartenUpdateRequest{
        @Schema(description = "어린이집 이름", example = "샛별 어린이집", required = true)
        private String kindergartenNm;

        @Schema(description = "어린이집 주소", example = "경기도 성남시", required = true)
        private String kindergartenAddr;

        @Schema(description = "어린이집 규모(정원, 크기)", example = "정원: 150명, 크기: 10m^2/명", required = true)
        private String kindergartenScale;

        @Schema(description = "어린이집 전화번호", example = "031-1234-5678", required = true)
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. 형식: 000-0000-0000")
        private String kindergartenNo;

        @Schema(description = "어린이집 운영시간", example = "7:00 - 22:00", required = true)
        private String kindergartenTime;

        @Schema(description = "어린이집 기타 정보", example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.", required = true)
        private String kindergartenInfo;

        @Schema(description = "어린이집 이미지 url", example = "path/file.png", required = true)
        private String kindergartenImage;
    }
}
