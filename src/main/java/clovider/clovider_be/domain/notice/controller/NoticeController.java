package clovider.clovider_be.domain.notice.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.service.NoticeCommandService;
import clovider.clovider_be.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping("/admin/notices")
    public ApiResponse<CustomResult> createNotice(@RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.createNotice(noticeRequest));
    }
}
