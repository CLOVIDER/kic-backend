package clovider.clovider_be.domain.qna.controller;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.service.QnaCommandService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
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
public class QnaController {

    private final QnaQueryService qnaQueryService;
    private final QnaCommandService qnaCommandService;

    @PostMapping("/qnas")
    public ApiResponse<CustomResult> createQna(@AuthEmployee Employee employee, @RequestBody QnaRequest qna) {
        return ApiResponse.onSuccess(qnaCommandService.createQna(employee, qna));
    }

    @PatchMapping("/qnas/{qnaId}")
    public ApiResponse<QnaUpdateResponse> updateQna(@PathVariable Long qnaId,
            @RequestBody QnaRequest qnaRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateQna(qnaId, qnaRequest));
    }

    @DeleteMapping("/qnas/{qnaId}")
    public ApiResponse<String> deleteQna(@PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaCommandService.deleteQna(qnaId));
    }

    @GetMapping("/qnas/{qnaId}")
    public ApiResponse<QnaResponse> getQna(@PathVariable Long qnaId) {
        return ApiResponse.onSuccess(qnaQueryService.getQna(qnaId));
    }

    @GetMapping("/qnas")
    public ApiResponse<CustomPage<QnaResponse>> getAllQnas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.onSuccess(qnaQueryService.getAllQnas(page, size));
    }
    
}
