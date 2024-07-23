package clovider.clovider_be.domain.notice.controller;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.service.NoticeCommandService;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
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
public class NoticeController {

    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryService noticeQueryService;

    @GetMapping("/notices/{noticeId}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeQueryService.getNotice(noticeId));
    }

    @GetMapping("/notices")
    public ApiResponse<CustomPage<NoticeResponse>> getAllNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(noticeQueryService.getAllNotices(page, size));
    }

    @PostMapping("/admin/notices")
    public ApiResponse<CustomResult> createNotice(@AuthEmployee Employee employee, @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.createNotice(employee, noticeRequest));
    }

    @PatchMapping("/admin/notices/{noticeId}")
    public ApiResponse<NoticeUpdateResponse> updateNotice(@PathVariable Long noticeId,
            @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.updateNotice(noticeId, noticeRequest));
    }

    @DeleteMapping("/admin/notices/{noticeId}")
    public ApiResponse<String> deleteNotice(@PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeCommandService.deleteNotice(noticeId));
    }

}
