package clovider.clovider_be.domain.kindergarten.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.service.KindergartenCommandService;
import clovider.clovider_be.domain.kindergarten.service.KindergartenQueryService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Kindergartens API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kindergartens")
public class KindergartenController {
    private final KindergartenCommandService kindergartenCommandService;
    private final KindergartenQueryService kindergartenQueryService;
    private final KindergartenImageCommandService kindergartenImageCommandService;

    @Operation(summary = "어린이집 정보 등록", description = "새로운 어린이집 상세정보를 등록하는 API입니다.")
    @Parameter(name = "kdgName", description = "어린이집 이름", required = true, example = "샛별 어린이집")
    @Parameter(name = "kdgAddress", description = "어린이집 주소", required = true, example = "경기도 성남시")
    @Parameter(name = "kdgScale", description = "어린이집 규모(정원, 크기)", required = true, example = "정원: 150명, 크기: 10m^2/명")
    @Parameter(name = "kdgNo", description = "어린이집 전화번호", required = true, example = "031-1234-5678")
    @Parameter(name = "kdgTime", description = "어린이집 운영시간", required = true, example = "7:00 - 22:00")
    @Parameter(name = "kdgInfo", description = "어린이집 기타 정보", required = true, example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<KindergartenResponse> registerKdg(HttpServletRequest request,
            @ModelAttribute KindergartenRequest kindergartenRequest) {

        KindergartenResponse result = kindergartenCommandService.registerKdg(
                kindergartenRequest.getKdgName(),
                kindergartenRequest.getKdgAddress(),
                kindergartenRequest.getKdgScale(),
                kindergartenRequest.getKdgNo(),
                kindergartenRequest.getKdgTime(),
                kindergartenRequest.getKdgInfo(),
                kindergartenRequest.getKdgImage());

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "어린이집 정보 삭제", description = "어린이집 상세 정보를 삭제하는 API입니다.")
    @Parameter(name = "kdgId", description = "어린이집 ID", required = true, example = "1")
    @DeleteMapping("/{kindergartenId}")
    public ApiResponse<CustomResult> deleteKdg(HttpServletRequest request,
            @PathVariable(name = "kindergartenId") Long kdgId) {

        CustomResult result = kindergartenCommandService.deleteKdg(kdgId);

        return ApiResponse.onSuccess(result);
    }

}
