package clovider.clovider_be.domain.recruit.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitQueryService recruitQueryService;
    private final LotteryQueryService lotteryQueryService;

    @GetMapping
    public ApiResponse<NowRecruitInfo> getRecruitInfo() {

        List<Recruit> recruits = recruitQueryService.getNowRecruitPeriod();
        List<CompetitionRate> recruitRates = lotteryQueryService.getRecruitRates(recruits);

        return ApiResponse.onSuccess(RecruitResponse.toNowRecruitInfo(recruits, recruitRates));
    }
}
