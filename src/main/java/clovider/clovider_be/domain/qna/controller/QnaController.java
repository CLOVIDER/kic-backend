package clovider.clovider_be.domain.qna.controller;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.dto.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.service.QnaCommandService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
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
@Tag(name = "QNA 관련 API 명세서", description = "QNA 관련 CRUD 작업을 처리하는 API")
public class QnaController {

    private final QnaQueryService qnaQueryService;
    private final QnaCommandService qnaCommandService;

    @Operation(summary = "Q&A 생성", description = "새로운 Q&A를 생성합니다.")
    @PostMapping("/qnas")
    public ApiResponse<CustomResult> createQna(
            @AuthEmployee Employee employee,
            @Valid @RequestBody QnaRequest qna) {
        return ApiResponse.onSuccess(qnaCommandService.createQna(employee, qna));
    }

    @Operation(summary = "Q&A 수정", description = "기존 Q&A를 수정합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @PatchMapping("/qnas/{qnaId}")
    public ApiResponse<QnaUpdateResponse> updateQna(
            @PathVariable Long qnaId,
            @Valid @RequestBody QnaRequest qnaRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateQna(qnaId, qnaRequest));
    }

    @Operation(summary = "Q&A 삭제", description = "Q&A를 삭제합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @DeleteMapping("/qnas/{qnaId}")
    public ApiResponse<String> deleteQna(
            @PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaCommandService.deleteQna(qnaId));
    }

    @Operation(summary = "Q&A 조회", description = "특정 Q&A의 세부정보를 조회합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @GetMapping("/qnas/{qnaId}")
    public ApiResponse<QnaResponse> getQna(
            @PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaQueryService.getQna(qnaId));
    }

    @Operation(summary = "전체 Q&A 목록 조회", description = "페이지네이션을 적용하여 전체 Q&A 목록을 조회합니다.")
    @GetMapping("/qnas")
    public ApiResponse<CustomPage<QnaResponse>> getAllQnas(
            @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기") int size) {
        return ApiResponse.onSuccess(qnaQueryService.getAllQnas(page, size));
    }

    @Operation(summary = "Q&A 답변 수정", description = "관리자가 Q&A에 답변을 수정합니다.")
    @Parameter(name = "qnaId", description = "qna ID", required = true, example = "1")
    @PatchMapping("/qnas/admin/{qnaId}")
    public ApiResponse<QnaUpdateResponse> updateQnaAnswer(
            @AuthEmployee Employee admin,
            @PathVariable Long qnaId,
            @Valid @RequestBody QnaAnswerRequest qnaAnswerRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateAnswer(admin, qnaId, qnaAnswerRequest));
    }

    @Operation(summary = "QNA 검색",
            description = "검색 타입과 키워드를 기반으로 QNA를 검색합니다.",
            parameters = {
                    @Parameter(name = "type", description = "검색 타입을 나타내는 Enum 값", example = "TITLE", required = true, in = ParameterIn.QUERY),
                    @Parameter(name = "keyword", description = "검색할 키워드", example = "공지", required = false, in = ParameterIn.QUERY)
            })
    @GetMapping("/qnas/search")
    public ApiResponse<List<QnaResponse>> searchQnas(
            @RequestParam SearchType type, @RequestParam(required = false) String keyword) {
        return ApiResponse.onSuccess(qnaQueryService.searchQnas(type, keyword));
    }
    
}
