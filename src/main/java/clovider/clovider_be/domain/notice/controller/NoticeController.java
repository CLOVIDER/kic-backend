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
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@Tag(name = "공지사항 관련 API 명세서", description = "공지사항 관련 CRUD 작업을 처리하는 API")
public class NoticeController {

    private final NoticeCommandService noticeCommandService;
    private final NoticeQueryService noticeQueryService;

    @Operation(summary = "공지사항 조회", description = "특정 공지사항의 정보를 조회합니다.")
    @Parameter(name = "noticeId", description = "공지사항 ID", required = true, example = "1")
    @GetMapping("/notices/{noticeId}")
    public ApiResponse<NoticeResponse> getNotice(@PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeQueryService.getNotice(noticeId));
    }

    @GetMapping("/notices")
    @Operation(summary = "전체 공지사항 목록 조회", description = "페이지네이션을 적용하여 전체 공지사항 목록을 조회합니다.")
    public ApiResponse<CustomPage<NoticeResponse>> getAllNotices(
            @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기") int size) {
        return ApiResponse.onSuccess(noticeQueryService.getAllNotices(page, size));
    }

    @Operation(summary = "공지사항 생성", description = "새로운 공지사항을 생성합니다.")
    @PostMapping("/admin/notices")
    public ApiResponse<CustomResult> createNotice(@AuthEmployee Employee employee,
            @Valid @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.createNotice(employee, noticeRequest));
    }
    @Operation(summary = "공지사항 수정", description = "특정 공지사항을 수정합니다.")
    @Parameter(name = "noticeId", description = "수정할 공지사항 ID", required = true, example = "1")
    @PatchMapping("/admin/notices/{noticeId}")
    public ApiResponse<NoticeUpdateResponse> updateNotice(@PathVariable Long noticeId,
            @Valid @RequestBody NoticeRequest noticeRequest) {
        return ApiResponse.onSuccess(noticeCommandService.updateNotice(noticeId, noticeRequest));
    }

    @Operation(summary = "공지사항 삭제",
            description = "특정 공지사항을 삭제합니다.")
    @Parameter(name = "noticeId", description = "삭제할 공지사항 ID", required = true, example = "1")
    @DeleteMapping("/admin/notices/{noticeId}")
    public ApiResponse<String> deleteNotice(@PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeCommandService.deleteNotice(noticeId));
    }

    @Operation(summary = "공지사항 검색",
            description = "검색 타입과 키워드를 기반으로 공지사항을 검색합니다.",
            parameters = {
            @Parameter(name = "type", description = "검색 타입을 나타내는 Enum 값", example = "TITLE", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "검색할 키워드", example = "공지", required = false, in = ParameterIn.QUERY)
    })
    @GetMapping("/notices/search")
    public ApiResponse<List<NoticeResponse>> searchNotices(
            @RequestParam SearchType type, @RequestParam(required = false) String keyword) {
        return ApiResponse.onSuccess(noticeQueryService.searchNotices(type, keyword));
    }

}
