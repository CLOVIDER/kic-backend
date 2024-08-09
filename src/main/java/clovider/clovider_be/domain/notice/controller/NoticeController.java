package clovider.clovider_be.domain.notice.controller;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.service.NoticeCommandService;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@Tag(name = "공지사항 관련 API 명세서", description = "공지사항 관련 CRUD 작업을 처리하는 API")
public class NoticeController {

    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryService noticeQueryService;

    /* 쿠키 활용 조회
    @Operation(summary = "공지사항 조회 - 공지사항 세부 페이지", description = "특정 공지사항의 정보를 조회합니다.")
    @Parameter(name = "noticeId", description = "공지사항 ID", required = true, example = "1")
    @GetMapping("/notices/{noticeId}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable Long noticeId, HttpServletRequest request, HttpServletResponse response) {
    return ApiResponse.onSuccess(noticeQueryService.getNotice(noticeId, request, response));
    }*/

    @Operation(summary = "공지사항 조회 - 공지사항 세부 페이지", description = "특정 공지사항의 정보를 조회합니다.")
    @Parameter(name = "noticeId", description = "공지사항 ID", required = true, example = "1")
    @GetMapping("/notices/{noticeId}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable Long noticeId, @AuthEmployee Employee employee,
            HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.onSuccess(noticeQueryService.getNotice(employee, noticeId, request, response));
    }

    @Operation(summary = "공지사항 생성 - 공지사항 작성 페이지", description = "새로운 공지사항을 생성합니다.")
    @PostMapping("/admin/notices")
    public ApiResponse<CustomResult> createNotice(@AuthEmployee Employee employee,
            @Valid @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.createNotice(employee, noticeRequest));
    }

    @Operation(summary = "공지사항 수정 - 공지사항 수정 페이지", description = "특정 공지사항을 수정합니다.")
    @Parameter(name = "noticeId", description = "수정할 공지사항 ID", required = true, example = "1")
    @PatchMapping("/admin/notices/{noticeId}")
    public ApiResponse<NoticeUpdateResponse> updateNotice(@PathVariable Long noticeId,
            @Valid @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.updateNotice(noticeId, noticeRequest));
    }

    @Operation(summary = "공지사항 삭제 - 공지사항 수정 페이지",
            description = "특정 공지사항을 삭제합니다.")
    @Parameter(name = "noticeId", description = "삭제할 공지사항 ID", required = true, example = "1")
    @DeleteMapping("/admin/notices/{noticeId}")
    public ApiResponse<String> deleteNotice(@PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeCommandService.deleteNotice(noticeId));
    }

    @GetMapping("/notices")
    @Operation(summary = "전체 공지사항 목록 조회 - 공지사항 리스트 페이지", description = "페이지네이션과 타입별 키워드 검색을 적용하여 전체 공지사항 목록을 조회합니다.")
    public ApiResponse<CustomPage<NoticeResponse>> getAllNotices(
            @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호") int page,
            @RequestParam(defaultValue = "3") @Parameter(description = "페이지 크기") int size,
            @RequestParam(required = false) SearchType type, @RequestParam(required = false) String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<NoticeResponse> allNotices = noticeQueryService.getAllNotices(pageRequest, type,
                keyword);
        return ApiResponse.onSuccess(new CustomPage<> (allNotices));
    }


}
