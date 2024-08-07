package clovider.clovider_be.domain.qna.controller;

import static clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.service.QnaCommandService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "QNA 관련 API 명세서", description = "QNA 관련 CRUD 작업을 처리하는 API")
public class QnaController {

    private final QnaQueryService qnaQueryService;
    private final QnaCommandService qnaCommandService;

    @Operation(summary = "Q&A 생성 - Q&A 작성 페이지", description = "새로운 Q&A를 생성합니다.")
    @PostMapping("/qnas")
    public ApiResponse<CustomResult> createQna(
            @AuthEmployee Employee employee,
            @Valid @RequestBody QnaCreateRequest qnaCreateRequest) {
        return ApiResponse.onSuccess(qnaCommandService.createQna(employee, qnaCreateRequest));
    }

    @Operation(summary = "Q&A 수정 - Q&A 수정 페이지", description = "기존 Q&A를 수정합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @PatchMapping("/qnas/{qnaId}")
    public ApiResponse<QnaUpdateResponse> updateQna(
            @PathVariable Long qnaId,
            @Valid @RequestBody QnaCreateRequest qnaCreateRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateQna(qnaId, qnaCreateRequest));
    }

    @Operation(summary = "Q&A 삭제 - Q&A 수정 페이지", description = "Q&A를 삭제합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @DeleteMapping("/qnas/{qnaId}")
    public ApiResponse<String> deleteQna(
            @PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaCommandService.deleteQna(qnaId));
    }

    @Operation(summary = "Q&A 조회 - Q&A 상세 페이지", description = "특정 Q&A의 세부정보를 조회합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @GetMapping("/qnas/{qnaId}")
    public ApiResponse<DetailedQnaResponse> getQna(@AuthEmployee Employee employee, @PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaQueryService.getQna(employee, qnaId));
    }

    @Operation(summary = "전체 Q&A 목록 조회 - Q&A 리스트 페이지", description = "페이지네이션과 타입별 키워드 검색을 적용하여 전체 Q&A 목록을 조회합니다.")
    @GetMapping("/qnas")
    public ApiResponse<CustomPage<BaseQnaResponse>> getAllQnas(
            @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호") int page,
            @RequestParam(defaultValue = "5") @Parameter(description = "페이지 크기") int size,
            @RequestParam(required = false) SearchType type, @RequestParam(required = false) String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<BaseQnaResponse> allQnas = qnaQueryService.getAllQnas(pageRequest, type, keyword);
        return ApiResponse.onSuccess(new CustomPage<> (allQnas));
    }

    @Operation(summary = "Q&A 답변 수정 - Q&A 답변 작성 페이지", description = "관리자가 Q&A에 답변을 수정합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @PatchMapping("/qnas/admin/{qnaId}")
    public ApiResponse<QnaUpdateResponse> updateQnaAnswer(
            @AuthEmployee Employee admin,
            @PathVariable Long qnaId,
            @Valid @RequestBody QnaRequest.QnaAnswerRequest qnaAnswerRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateAnswer(admin, qnaId, qnaAnswerRequest));
    }
    
}
