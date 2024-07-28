package clovider.clovider_be.domain.admin.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.mail.service.MailService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final LotteryQueryService lotteryQueryService;
    private final MailService mailService;

    @Operation(summary = "어린이집 모집 결과 이메일 전송 API", description = "해당 모집의 추첨결과를 이메일로 전송합니다.")
    @PostMapping("/emails/recruits/{recruitId}")
    @Parameter(name = "recruitId", description = "모집 ID", required = true)
    public ApiResponse<String> sendRecruitResult(@PathVariable(name = "recruitId") Long recruitId) {

        List<RecruitResult> recruitResult = lotteryQueryService.getRecruitResult(recruitId);
        mailService.sendRecruitResult(recruitResult);

        return ApiResponse.onSuccess("성공적으로 추첨결과가 전송되었습니다.");
    }

}
