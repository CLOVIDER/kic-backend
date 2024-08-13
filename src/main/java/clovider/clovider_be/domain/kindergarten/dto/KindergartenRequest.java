package clovider.clovider_be.domain.kindergarten.dto;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class KindergartenRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class KindergartenRegisterRequest{
        @Schema(description = "어린이집 이름", example = "샛별 어린이집", required = true)
        @NotNull(message = "어린이집 이름은 필수 항목입니다.")
        private String kindergartenNm;

        @Schema(description = "어린이집 주소", example = "경기도 성남시", required = true)
        @NotNull(message = "어린이집 주소는 필수 항목입니다.")
        private String kindergartenAddr;

        @Schema(description = "어린이집 규모(평수)", example = "500", required = true)
        @NotNull(message = "어린이집 규모는 필수 항목입니다.")
        private Integer kindergartenScale;

        @Schema(description = "어린이집 정원(명수)", example = "100", required = true)
        @NotNull(message = "어린이집 정원은 필수 항목입니다.")
        private Integer kindergartenCapacity;

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

        @Schema(
                description = "어린이집 분반 정보",
                example = "[{\"className\":\"햇님반\", \"ageClass\":\"TODDLER\"}, {\"className\":\"달님만\", \"ageClass\":\"INFANT\"}]",
                required = true
        )
        @NotNull(message = "어린이집 분반 정보는 필수 항목입니다.")
        private List<KindergartenClassDTO> kindergartenClass;

        @Schema(description = "어린이집 이미지 url", example = "[\"path/file1.png\", \"path/file2.png\"]", required = true)
        @NotNull(message = "어린이집 이미지 url은 필수 항목입니다.")
        private List<String> kindergartenImages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class KindergartenUpdateRequest{
        @Schema(description = "어린이집 이름", example = "샛별 어린이집", required = true)
        private String kindergartenNm;

        @Schema(description = "어린이집 주소", example = "경기도 성남시", required = true)
        private String kindergartenAddr;

        @Schema(description = "어린이집 규모(평수)", example = "500", required = true)
        private Integer kindergartenScale;

        @Schema(description = "어린이집 정원(명수)", example = "100", required = true)
        private Integer kindergartenCapacity;

        @Schema(description = "어린이집 전화번호", example = "031-1234-5678", required = true)
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. 형식: 000-0000-0000")
        private String kindergartenNo;

        @Schema(description = "어린이집 운영시간", example = "7:00 - 22:00", required = true)
        private String kindergartenTime;

        @Schema(description = "어린이집 기타 정보", example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.", required = true)
        private String kindergartenInfo;

        @Schema(
                description = "어린이집 분반 정보",
                example = "[{\"className\":\"햇님반\", \"ageClass\":\"TODDLER\"}, {\"className\":\"달님만\", \"ageClass\":\"INFANT\"}]",
                required = true
        )
        private List<KindergartenClassDTO> kindergartenClass;

        @Schema(description = "어린이집 이미지 url", example = "[\"path/file1.png\", \"path/file2.png\"]", required = true)
        private List<String> kindergartenImages;
    }
}
