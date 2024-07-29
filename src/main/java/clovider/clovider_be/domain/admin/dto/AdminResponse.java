package clovider.clovider_be.domain.admin.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.global.util.TimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class AdminResponse {

    @Schema(description = "관리자 대시보드 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DashBoard {

        @Schema(description = "현재 모집 중인 어린이집 정보")
        private NowRecruitInfo recruitInfo;
        @Schema(description = "총 신청자 수")
        private Long totalApplications;
        @Schema(description = "승인 대기 수")
        private Long unAcceptApplications;
        @Schema(description = "어린이집별 승인 현황")
        private List<AcceptResult> acceptResults;
        @Schema(description = "최신 3개의 공지사항")
        private List<NoticeTop3> noticeTop3;
        @Schema(description = "QNA 답변 대기 수")
        private Integer waitQnaCnt;

    }

    public static DashBoard toDashBoard(NowRecruitInfo recruitInfo,
            Long totalApplications, Long unAcceptApplications,
            List<AcceptResult> acceptResults, List<NoticeTop3> noticeTop3, Integer waitQnaCnt) {

        return DashBoard.builder()
                .recruitInfo(recruitInfo)
                .totalApplications(totalApplications)
                .unAcceptApplications(unAcceptApplications)
                .acceptResults(acceptResults)
                .noticeTop3(noticeTop3)
                .waitQnaCnt(waitQnaCnt)
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationPage {

        private List<ApplicationList> content;
        private int totalPage;
        private long totalElements;
        private int size;
        private int currPage;
        private Boolean hasNext;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationList {

        private String createdAt;
        private String accountId;
        private String employeeNo;
        private Long applicationId;
        private String isAccept;
    }

    public static ApplicationList toApplicationList(Lottery lottery, Application application) {

        return ApplicationList.builder()
                .createdAt(TimeUtil.formattedDateTime(application.getCreatedAt()))
                .accountId(application.getEmployee().getAccountId())
                .employeeNo(application.getEmployee().getEmployeeNo())
                .applicationId(application.getId())
                .isAccept(lottery.getIsAccept().getDescription())
                .build();
    }

    public static ApplicationPage toApplicationPage(Page<Lottery> lotteries,
            List<Application> applications) {

        List<ApplicationList> applicationLists = IntStream.range(0, applications.size())
                .mapToObj(
                        i -> toApplicationList(lotteries.getContent().get(i), applications.get(i)))
                .toList();

        return ApplicationPage.builder()
                .content(applicationLists)
                .totalPage(lotteries.getTotalPages())
                .totalElements(lotteries.getTotalElements())
                .size(lotteries.getSize())
                .currPage(lotteries.getNumber())
                .hasNext(lotteries.hasNext())
                .isFirst(lotteries.isFirst())
                .isLast(lotteries.isLast())
                .build();
    }
}