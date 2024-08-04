package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.enums.DocumentType;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.recruit.Recruit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
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

    @Schema(description = "(어린이 : 모집ID) 리스트", example = "")
    private List<Map<String, Object>> childrenRecruitList;

    @Schema(description = "증빙 서류 URL 리스트", example = "['s3-1', 's3-2']")
    private Map<DocumentType, String> imageUrls;
}