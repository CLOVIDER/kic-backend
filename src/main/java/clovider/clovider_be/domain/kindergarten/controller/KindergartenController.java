package clovider.clovider_be.domain.kindergarten.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenUpdateResponse;
import clovider.clovider_be.domain.kindergarten.service.KindergartenCommandService;
import clovider.clovider_be.domain.kindergarten.service.KindergartenQueryService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "kindergartens-controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/kindergartens")
public class KindergartenController {
    private final KindergartenCommandService kindergartenCommandService;
    private final KindergartenQueryService kindergartenQueryService;
    private final KindergartenImageCommandService kindergartenImageCommandService;

    @Operation(summary = "어린이집 정보 등록", description = "새로운 어린이집 상세정보를 등록하는 API입니다.")
    @PostMapping
    public ApiResponse<KindergartenRegisterResponse> registerKindergarten(
            @Valid @RequestBody KindergartenRequest.KindergartenRegisterRequest request) {

        KindergartenRegisterResponse result = kindergartenCommandService.registerKindergarten(request);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "어린이집 정보 삭제", description = "어린이집 상세 정보를 삭제하는 API입니다.")
    @Parameter(name = "kdgId", description = "어린이집 ID", required = true, example = "1")
    @DeleteMapping("/{kindergartenId}")
    public ApiResponse<CustomResult> deleteKindergarten(@PathVariable("kindergartenId") Long kindergartenId) {

        CustomResult result = kindergartenCommandService.deleteKindergarten(kindergartenId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "어린이집 정보 일부 수정", description = "어린이집 정보를 일부 수정하는 API입니다.")
    @Parameter(name = "kdgId", description = "어린이집 ID", required = true, example = "1")
    @PatchMapping("/{kindergartenId}")
    public ApiResponse<KindergartenUpdateResponse> updateKindergarten(
            @RequestBody KindergartenUpdateRequest request,
            @PathVariable("kindergartenId") Long kindergartenId) {

        KindergartenUpdateResponse result = kindergartenCommandService.updateKindergarten(kindergartenId, request);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "개별 어린이집 정보 불러오기", description = "어린이집 개별 정보를 불러오는 API입니다.")
    @Parameter(name = "kdgId", description = "어린이집 ID", required = true, example = "1")
    @GetMapping("/{kindergartenId}")
    public ApiResponse<KindergartenGetResponse> getKindergartenInfo(
            @PathVariable("kindergartenId") Long kindergartenId) {

        KindergartenGetResponse result = kindergartenQueryService.getKindergarten(kindergartenId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "전체 어린이집 정보 불러오기", description = "전체 어린이집 정보를 불러오는 API입니다.")
    @GetMapping("")
    public ApiResponse<List<KindergartenGetResponse>> getAllKindergartenInfo() {

        List<KindergartenGetResponse> result = kindergartenQueryService.getAllKindergartens();

        return ApiResponse.onSuccess(result);
    }


}
