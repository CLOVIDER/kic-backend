package clovider.clovider_be.domain.kindergartenClass.dto;

import clovider.clovider_be.domain.enums.AgeClass;
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

    @Schema(description = "분반 나이대", example = "INFANT", required = true)
    @NotNull(message = "분반 나이대는 필수 항목입니다.")
    private AgeClass ageClass;

}
