package clovider.clovider_be.domain.qna.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.service.QnaCommandService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QnaController {

    private final QnaQueryService qnaQueryService;
    private final QnaCommandService qnaCommandService;

    @PostMapping("/qnas")
    public ApiResponse<CustomResult> createQna(@RequestBody QnaRequest qna) {
        return ApiResponse.onSuccess(qnaCommandService.createQna(qna));
    }

    @PatchMapping("/qnas/{qnaId}")
    public ApiResponse<CustomResult> updateQna(@PathVariable Long qnaId,
            @RequestBody QnaRequest qnaRequest) {
        return ApiResponse.onSuccess(qnaCommandService.updateQna(qnaId, qnaRequest));
    }
}
