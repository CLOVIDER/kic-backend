package clovider.clovider_be.domain.admin.controller;

import static clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationStatus;
import static clovider.clovider_be.domain.admin.dto.AdminResponse.toDashBoard;
import static clovider.clovider_be.domain.admin.dto.AdminResponse.toNotDashBoard;

import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitCreationRequest;
import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.AdminResponse.DashBoard;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitCreationInfo;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.domain.mail.service.MailService;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.domain.recruit.service.RecruitCommandService;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import clovider.clovider_be.global.util.PdfUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final ApplicationQueryService applicationQueryService;
    private final RecruitCommandService recruitCommandService;
    private final PdfUtil pdfUtil;
    private final MailService mailService;

    @Operation(summary = "[전체] 관리자 대시보드 조회", description = "어린이집 모집 통계 정보를 조회합니다.")
    @GetMapping("/dashboards")
    public ApiResponse<DashBoard> getDashboard() {

        // 진행 중인 모집 정보, 기간, 경쟁률
        List<NowRecruit> recruits = recruitQueryService.getNowRecruitOrderByClass(
                        LocalDateTime.now())
                .getNowRecruits();

        // Top3 공지글
        List<NoticeTop3> noticeTop3 = noticeQueryService.getTop3Notices();

        // 답변 대기 수
        Integer waitQna = qnaQueryService.getWaitQna();

        NowRecruitInfo nowRecruitInfo;

        if (recruits.isEmpty()) {
            nowRecruitInfo = RecruitResponse.toNotRecruitInfo();
            return ApiResponse.onSuccess(
                    toNotDashBoard(nowRecruitInfo, noticeTop3, waitQna));
        }
        List<CompetitionRate> recruitRates = lotteryQueryService.getRecruitRates(recruits);
        nowRecruitInfo = RecruitResponse.toNowRecruitInfo(recruits, recruitRates);

        List<Long> recruitIds = recruits.stream().map(NowRecruit::getId).toList();

        // 총 신청자 수
        Long totalApplication = lotteryQueryService.getTotalApplication(recruitIds);

        // 승인 대기 수
        Long unAcceptApplication = lotteryQueryService.getUnAcceptApplication(
                recruitIds);

        // 신청 현황
        List<AcceptResult> acceptStatus = lotteryQueryService.getAcceptResult(recruitIds);

        return ApiResponse.onSuccess(
                toDashBoard(nowRecruitInfo, totalApplication, unAcceptApplication, acceptStatus,
                        noticeTop3, waitQna));
    }

    @Operation(summary = "[컴포넌트] 관리자 대시보드 최신글 3개 조회 API", description = "최신 글 3개 조회")
    @GetMapping("/notices/top3")
    public ApiResponse<List<NoticeTop3>> getNoticeTop3() {

        return ApiResponse.onSuccess(noticeQueryService.getTop3Notices());
    }

    @Operation(summary = "[컴포넌트] 관리자 대시보드 답변 대기 개수 조회 API", description = "QNA 답변 대기 수 조회")
    @GetMapping("/qnas/waiting")
    public ApiResponse<Integer> getWaitQna() {

        return ApiResponse.onSuccess(qnaQueryService.getWaitQna());
    }

    @Operation(summary = "[컴포넌트] 관리자 대시보드 모집 신청 현황 조회 API", description = "총 신청자 수, 승인 대기 수 조회")
    @GetMapping("/recruits/applications/status")
    public ApiResponse<ApplicationStatus> getApplicationsStatus() {

        List<Long> recruitIds = recruitQueryService.getRecruitIngAndScheduled(LocalDateTime.now());
        Long totalCnt = lotteryQueryService.getTotalApplication(recruitIds);
        Long unAcceptCnt = lotteryQueryService.getUnAcceptApplication(
                recruitIds);

        return ApiResponse.onSuccess(new ApplicationStatus(totalCnt, unAcceptCnt));
    }

    @Operation(summary = "[컴포넌트] 관리자 대시보드 어린이집별 신청 현황 조회 API", description = "어린이집별 승인, 승인대기, 미승인 현황 조회")
    @GetMapping("/recruits/kindergartens/status")
    public ApiResponse<List<AcceptResult>> getAcceptStatus() {

        List<Long> recruitIds = recruitQueryService.getRecruitIngAndScheduled(LocalDateTime.now());

        return ApiResponse.onSuccess(lotteryQueryService.getAcceptResult(recruitIds));
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
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "size", description = "페이지 크기"),
            @Parameter(name = "filter", description = "승인 여부 필터링"),
            @Parameter(name = "q", description = "신청자 이름 검색")
    })
    @GetMapping("/recruits/applications")
    public ApiResponse<CustomPage<ApplicationList>> findRecruitsApplications(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "filter", defaultValue = "ALL", required = false) String filter,
            @RequestParam(name = "q", required = false) String value) {

        PageRequest pageRequest = PageRequest.of(page, size);
        SearchVO searchVO = SearchVO.of(filter, value);

        List<Recruit> recruits = recruitQueryService.getNowRecruit(LocalDateTime.now());

        List<Long> applicationIds = lotteryQueryService.getApplicationsByLotteries(
                recruits);

        Page<ApplicationList> applicationPage = applicationQueryService.getNowApplications(
                applicationIds, pageRequest, searchVO);

        return ApiResponse.onSuccess(new CustomPage<>(applicationPage));
    }

    @Operation(summary = "모집 결과 PDF 파일로 다운하기", description = "특정 모집의 결과를 조회하여 PDF 파일을 다운합니다.")
    @Parameter(name = "recruitId", description = "모집 ID")
    @PostMapping("/recruits/{recruitId}/export")
    public ResponseEntity<ByteArrayResource> exportLottery(
            @PathVariable("recruitId") Long recruitId) {

        List<RecruitResult> recruitResult = lotteryQueryService.getRecruitResult(recruitId);
        RecruitInfo recruitInfo = recruitQueryService.getRecruitInfo(recruitId);

        byte[] pdfBytes = pdfUtil.resultPdf(recruitResult, recruitInfo);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }


    @Operation(summary = "종료된 모집에 대한 추첨 결과 리스트 조회 - 관리자 추첨 결과 탭 ", description = "종료된 모집의 추첨 결과를 리스트 형식으로 조회합니다")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "size", description = "페이지 크기"),
            @Parameter(name = "accountId", description = "신청자 아이디 검색")
    })
    @GetMapping("/lotteries/result/{kindergartenId}")
    public ApiResponse<CustomPage<LotteryResult>> getLotteryResult(
            @PathVariable(name = "kindergartenId") Long kindergartenId,
            @RequestParam(name = "class", required = true) Integer ageClass,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "accountId", required = false) String value) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LotteryResult> lotteryResultPage = lotteryQueryService.getLotteryResultByLotteryId(
                ageClass, kindergartenId,
                pageRequest, value);

        return ApiResponse.onSuccess(new CustomPage<>(lotteryResultPage));
    }

    @Operation(summary = "생성한 모집 정보 조회 - 관리자 모집 조회 페이지", description = "생성한 모집을 어린이집, 분반 정보를 포함하여 조회합니다.")
    @GetMapping("/recruits")
    public ApiResponse<RecruitCreationInfo> getRecruitCreationInfo() {
        return ApiResponse.onSuccess(recruitQueryService.getRecruitCreationInfo());
    }

    @Operation(summary = "관리자가 모집을 수정한다.", description = "관리자가 이미 생성된 모집을 수정한다.")
    @PatchMapping("/recruits")
    public ApiResponse<String> updateRecruit(
            @RequestBody RecruitCreationRequest request, @PathVariable Long recruitId) {

        return ApiResponse.onSuccess(recruitCommandService.updateRecruit(request, recruitId));
    }

    @Operation(summary = "모집 생성", description = "관리자가 모집을 생성합니다.")
    @PostMapping("/recruits")
    public ApiResponse<String> createRecruit(@RequestBody RecruitCreationRequest request) {
        return ApiResponse.onSuccess(recruitCommandService.createRecruit(request));
    }


}
