package clovider.clovider_be.domain.admin.controller;

import static clovider.clovider_be.domain.admin.dto.AdminResponse.DashBoard;
import static clovider.clovider_be.domain.admin.dto.AdminResponse.toDashBoard;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QnaQueryService qnaQueryService;
    private final NoticeQueryService noticeQueryService;
    private final RecruitQueryService recruitQueryService;
    private final LotteryQueryService lotteryQueryService;


    @GetMapping("/dashboards")
    public ApiResponse<DashBoard> getDashboard() {

        // 진행 중인 모집 정보, 기간, 경쟁률
        List<Recruit> recruits = recruitQueryService.getNowRecruitPeriod();
        List<CompetitionRate> recruitRates = lotteryQueryService.getRecruitRates(recruits);
        NowRecruitInfo nowRecruitInfo = RecruitResponse.toNowRecruitInfo(recruits, recruitRates);

        // 총 신청자 수
        Long totalApplication = lotteryQueryService.getTotalApplication(recruits);

        // 승인 대기 수
        Long unAcceptApplication = lotteryQueryService.getUnAcceptApplication(
                recruits);

        // 신청 현황
        List<AcceptResult> acceptStatus = lotteryQueryService.getAcceptStatus(recruits);

        // Top3 공지글
        List<NoticeTop3> noticeTop3 = noticeQueryService.getTop3Notices();

        // 답변 대기 수
        Integer waitQna = qnaQueryService.getWaitQna();

        return ApiResponse.onSuccess(
                toDashBoard(nowRecruitInfo, totalApplication, unAcceptApplication, acceptStatus,
                        noticeTop3, waitQna));
    }
}
