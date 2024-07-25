package clovider.clovider_be.domain.recruit.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recruit 관련 API 명세서")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitQueryService recruitQueryService;
    private final LotteryQueryService lotteryQueryService;

    @Operation(summary = "일반 사용자(사내 직원) 랜딩 페이지 조회 API", description = "현재 진행 중인 어린이집 모집에 대해 조회하는 API 입니다.")
    @GetMapping
    public ApiResponse<NowRecruitInfo> getRecruitInfo() {

        List<Recruit> recruits = recruitQueryService.getNowRecruitPeriod();
        List<CompetitionRate> recruitRates = lotteryQueryService.getRecruitRates(recruits);

        return ApiResponse.onSuccess(RecruitResponse.toNowRecruitInfo(recruits, recruitRates));
    }
}
