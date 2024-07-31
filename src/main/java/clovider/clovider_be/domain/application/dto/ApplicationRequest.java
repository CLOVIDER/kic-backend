package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.recruit.Recruit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "신청서 요청 DTO")
public class ApplicationRequest {

    //workYears는 기존 사용자 정보로 입력

    @Schema(description = "한부모 가정 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "0")
    private Character isSingleParent;

    @Schema(description = "자녀 수 (해당되는 자녀 수 만큼 입력)", example = "3")
    private Integer childrenCnt;

    @Schema(description = "장애 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
    private Character isDisability;

    @Schema(description = "맞벌이 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
    private Character isDualIncome;

    @Schema(description = "사내 커플 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
    private Character isEmployeeCouple;

    @Schema(description = "형제 자매 재원 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
    private Character isSibling;

    @Schema(description = "자녀 이름", example = "KIM")
    private String childNm;

    @Schema(description = "지원한 모집 ID 리스트", example = "[1, 2, 5]")
    private List<Long> recruitIdList;

    @Schema(description = "증빙 서류 리스트", example = "['s3-1', 's3-2']")
    private List<String> documents;
}