package clovider.clovider_be.domain.application.controller;

import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.application.dto.ApplicationWriteDto;
import clovider.clovider_be.domain.application.service.ApplicationCommandService;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.common.CustomResult;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    @Autowired
    private ApplicationCommandService applicationCommandService;
    private ApplicationQueryService applicationQueryService;

    @PostMapping("/applications")
    public ApiResponse<CustomResult> createApplication(@RequestBody ApplicationWriteDto dto) {
        return ApiResponse.onSuccess(applicationCommandService.applicationCreate(dto));
    }

    @PatchMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> updateApplication(@PathVariable Long applicationId, @RequestBody ApplicationUpdateDto dto) {
        return ApiResponse.onSuccess(applicationCommandService.applicationUpdate(applicationId, dto));
    }

    @DeleteMapping("/applications/{applicationId}")
    public ApiResponse<CustomResult> deleteApplication(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(applicationCommandService.applicationDelete(applicationId));
    }
}
