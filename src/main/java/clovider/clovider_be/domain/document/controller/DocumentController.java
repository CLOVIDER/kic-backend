package clovider.clovider_be.domain.document.controller;

import clovider.clovider_be.domain.document.dto.DocumentResponse.DocumentInfo;
import clovider.clovider_be.domain.document.service.DocumentQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "증빙서류 관련 API 명세서", description = "증빙서류 관련 CRUD 작업을 처리하는 API")
public class DocumentController {
    private final DocumentQueryService documentQueryService;

    @Operation(summary = "증빙서류 리스트 조회 - 관리자 신청 승인 페이지", description = "특정 신청서에 제출된 증빙서류 리스트 정보를 조회합니다.")
    @Parameter(name = "applicationId", description = "신청서 ID", required = true, example = "1")
    @GetMapping("/admin/documents/{applicationId}")
    public ApiResponse<List<DocumentInfo>> getDocumentInfos(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(documentQueryService.getDocumentInfos(applicationId));
    }
}
