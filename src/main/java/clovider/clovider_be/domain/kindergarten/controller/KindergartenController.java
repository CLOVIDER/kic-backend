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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Kindergartens API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/kindergartens")
public class KindergartenController {
    private final KindergartenCommandService kindergartenCommandService;
    private final KindergartenQueryService kindergartenQueryService;
    private final KindergartenImageCommandService kindergartenImageCommandService;

    @Operation(summary = "어린이집 정보 등록", description = "새로운 어린이집 상세정보를 등록하는 API입니다.")
    @PostMapping
    public ApiResponse<KindergartenResponse> registerKdg(HttpServletRequest request,
            @Valid @RequestBody  KindergartenRequest kindergartenRequest) {

        KindergartenResponse result = kindergartenCommandService.registerKdg(kindergartenRequest);

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
