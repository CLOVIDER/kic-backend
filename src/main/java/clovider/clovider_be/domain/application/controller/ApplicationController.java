package clovider.clovider_be.domain.application.controller;

import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.application.service.ApplicationCommandService;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "신청서 관련 API 명세서", description = "신청서 CRUD 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    @Autowired
    private ApplicationCommandService applicationCommandService;

    @Autowired
    private ApplicationQueryService applicationQueryService;

    @Operation(summary = "신청서 작성 API", description = "지원한 모집 ID 리스트에 대해서도 입력받습니다.")
    @PostMapping("/applications")
    public ApiResponse<CustomResult> createApplication(@RequestBody ApplicationRequest dto, @AuthEmployee
            Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationCreate(dto, employee));
    }

    @Operation(summary = "신청서 수정 API", description = "")
    @PatchMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> updateApplication(@PathVariable Long applicationId, @RequestBody ApplicationRequest dto, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationUpdate(applicationId, dto));
    }

    @Operation(summary = "신청서 삭제 API", description = "")
    @DeleteMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> deleteApplication(@PathVariable Long applicationId, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationDelete(applicationId));
    }

    @Operation(summary = "신청서 조회 API", description = "")
    @GetMapping("/applications/{applicationId}")
    public ApiResponse<ApplicationResponse> getApplication(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(applicationQueryService.applicationIdRead(applicationId));
    }

    @Operation(summary = "신청서 리스트 조회 API", description = "")
    @GetMapping("/applications")
    public ApiResponse<CustomPage<ApplicationResponse>> getApplicationList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(applicationQueryService.applicationListRead(page, size));
    }

    @Operation(summary = "신청서 임시 저장 API", description = "")
    @PostMapping("/applications/tmp")
    public ApiResponse<CustomResult> createApplicationTmp(@RequestBody ApplicationRequest dto, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationTempSave(dto, employee));
    }

    @Operation(summary = "관리자 신청서 승인 API", description = "acceptRequest 값을 body로 '0' 또는 '1로' 입력받습니다. 0 이면 미승인 처리, 1 이면 승인 처리로 수정됩니다.")
    @PatchMapping("/admin/applications/{applicationId}")
    public ApiResponse<CustomResult> acceptApplication(@PathVariable Long applicationId, @RequestBody
    @Schema(description = "신청서 승인 여부", example = "{'accessRequest' : '0'}") Map<String, String> acceptRequest) {

        String acceptStatus = acceptRequest.get("acceptRequest");
        Accept accept = Accept.WAIT;

        if ("1".equals(acceptStatus)) {
            accept = Accept.ACCEPT;
        } else if ("0".equals(acceptStatus)) {
            accept = Accept.UNACCEPT;
        }

        return ApiResponse.onSuccess(applicationCommandService.applicationAccept(applicationId, accept));
    }


}
