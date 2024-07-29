package clovider.clovider_be.domain.admin.dto;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
