package clovider.clovider_be.domain.admin.controller;

import static clovider.clovider_be.domain.admin.dto.AdminResponse.toDashBoard;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationPage;
import clovider.clovider_be.domain.admin.dto.AdminResponse.DashBoard;
import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.mail.service.MailService;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 기능 관련 API 명세서")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final QnaQueryService qnaQueryService;
    private final NoticeQueryService noticeQueryService;
    private final RecruitQueryService recruitQueryService;
    private final LotteryQueryService lotteryQueryService;
    private final MailService mailService;
    private final ApplicationQueryService applicationQueryService;


    @Operation(summary = "관리자 대시보드 조회", description = "어린이집 모집 통계 정보를 조회합니다.")
    @GetMapping("/dashboards")
    public ApiResponse<DashBoard> getDashboard() {

        // 진행 중인 모집 정보, 기간, 경쟁률
        List<Recruit> recruits = recruitQueryService.getNowRecruitOrderByClass();
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

    @Operation(summary = "어린이집 모집 결과 이메일 전송 API", description = "해당 모집의 추첨결과를 이메일로 전송합니다.")
    @PostMapping("/emails/recruits/{recruitId}")
    @Parameter(name = "recruitId", description = "모집 ID", required = true)
    public ApiResponse<String> sendRecruitResult(@PathVariable(name = "recruitId") Long recruitId) {

        List<RecruitResult> recruitResult = lotteryQueryService.getRecruitResult(recruitId);
        mailService.sendRecruitResult(recruitResult);

        return ApiResponse.onSuccess("성공적으로 추첨결과가 전송되었습니다.");
    }


    @Operation(summary = "진행중인 모집의 신청 현황 조회", description = "진행 중인 모집의 모든 신청 내역을 조회합니다.")
    @GetMapping("/recruits/applications")
    public ApiResponse<ApplicationPage> findRecruitsApplications(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        List<Recruit> recruits = recruitQueryService.getNowRecruit();

        Page<Lottery> lotteryPage = lotteryQueryService.getNowLotteries(recruits,
                pageRequest);

        List<Application> applications = applicationQueryService.getNowApplications(
                lotteryPage.getContent());

        return ApiResponse.onSuccess(AdminResponse.toApplicationPage(lotteryPage, applications));
    }
}
