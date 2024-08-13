package clovider.clovider_be.domain.kindergartenClass.dto;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KindergartenClassDTO {

    @Schema(description = "분반 이름", example = "햇님반", required = true)
    @NotNull(message = "어린이집 분반 이름은 필수 항목입니다.")
    private String className;

    @Schema(description = "분반 나이대", example = "0", required = true)
    @NotNull(message = "분반 나이대는 필수 항목입니다.")
    private int ageClass;

    @Schema(description = "분반 나이대 (문자열 형태)", example = "0세")
    private String ageClassString;

    public KindergartenClassDTO toKindergartenClassResponse() {
        return KindergartenClassDTO.builder()
                .className(this.className)
                .ageClassString(this.ageClass + "세")
                .build();
    }
}
