package clovider.clovider_be.domain.application.controller;

import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.application.service.ApplicationCommandService;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    @Autowired
    private ApplicationCommandService applicationCommandService;

    @Autowired
    private ApplicationQueryService applicationQueryService;

    @PostMapping("/applications")
    public ApiResponse<CustomResult> createApplication(@RequestBody ApplicationRequest dto, @AuthEmployee
            Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationCreate(dto, employee));
    }

    @PatchMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> updateApplication(@PathVariable Long applicationId, @RequestBody ApplicationRequest dto, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationUpdate(applicationId, dto));
    }

    @DeleteMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> deleteApplication(@PathVariable Long applicationId, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationDelete(applicationId));
    }

    @GetMapping("/applications/{applicationId}")
    public ApiResponse<ApplicationResponse> getApplication(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(applicationQueryService.applicationIdRead(1L));
    }

    @GetMapping("/applications")
    public ApiResponse<CustomPage<ApplicationResponse>> getApplicationList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(applicationQueryService.applicationListRead(page, size));
    }

    @PostMapping("/applications/tmp")
    public ApiResponse<CustomResult> createApplicationTmp(@RequestBody ApplicationRequest dto, @AuthEmployee
    Employee employee) {
        return ApiResponse.onSuccess(applicationCommandService.applicationTempSave(dto, employee));
    }
}
