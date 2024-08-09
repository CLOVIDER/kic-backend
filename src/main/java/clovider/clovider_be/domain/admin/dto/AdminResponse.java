package clovider.clovider_be.domain.admin.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruitInfo;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
import clovider.clovider_be.global.util.TimeUtil;
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

    public static DashBoard toNotDashBoard(NowRecruitInfo recruitInfo, List<NoticeTop3> noticeTop3s,
            Integer waitQnaCnt) {

        return DashBoard.builder()
                .recruitInfo(recruitInfo)
                .noticeTop3(noticeTop3s)
                .waitQnaCnt(waitQnaCnt)
                .build();
    }

    @Schema(description = "[관리자 대시보드] 총 신청자 수, 승인 대기 응답 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationStatus {

        @Schema(description = "총 신청자 수")
        private Long totalApplications;
        @Schema(description = "승인 대기 수")
        private Long unAcceptApplications;
    }

    @Schema(description = "어린이집별 신청현황 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AcceptResult {

        @Schema(description = "어린이집 이름")
        private String kindergartenNm;
        @Schema(description = "승인 수")
        private Integer acceptCnt;
        @Schema(description = "미승인 수")
        private Integer unAcceptCnt;
        @Schema(description = "승인 대기 수")
        private Integer waitCnt;
    }

    //** ===================================================================================== **//

    @Schema(description = "신청내역 페이징 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationPage {

        @Schema(description = "신청내역 데이터 리스트")
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

        @Schema(description = "신청서 작성일", example = "2024.07.25")
        private String createdAt;
        @Schema(description = "신청자 이름", example = "김성민")
        private String nameKo;
        @Schema(description = "신청자 사내 번호", example = "201930303")
        private String employeeNo;
        @Schema(description = "신청서 ID", example = "1")
        private Long applicationId;
        @Schema(description = "신청서 승인여부", example = "승인")
        private String isAccept;
    }

    public static ApplicationList toApplicationList(Application application) {

        return ApplicationList.builder()
                .createdAt(TimeUtil.formattedDate(application.getCreatedAt()))
                .nameKo(application.getEmployee().getNameKo())
                .employeeNo(application.getEmployee().getEmployeeNo())
                .applicationId(application.getId())
                .isAccept(application.getIsAccept().getDescription())
                .build();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LotteryResult {
        private String employeeNo;
        private String nameKo;
        private String childNm;
        private String lotteryResult;
        private Long applicationId;
    }

    public static LotteryResult toLotteryResult(Lottery lottery) {
        String description = lottery.getResult().getDescription();
        String lotteryResult;

        // 'description'이 "낙첨"일 경우 'lottery.rankNo'로 치환
        if ("낙첨".equals(description)) {
            lotteryResult = "대기 " + lottery.getRankNo().toString();
        } else {
            lotteryResult = description;
        }

        return LotteryResult.builder()
                .employeeNo(lottery.getApplication().getEmployee().getEmployeeNo())
                .nameKo(lottery.getApplication().getEmployee().getNameKo())
                .childNm(lottery.getChildNm())
                .lotteryResult(lotteryResult)
                .applicationId(lottery.getApplication().getId())
                .build();
    }

    @Schema(description = "분반 정보 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitClassInfo {
        @Schema(description = "분반 이름", example = "0~3세반")
        private String ageClass;

        @Schema(description = "모집인원", example = "20")
        private Integer recruitCnt;
    }

    public static RecruitClassInfo toRecruitClassInfo(Recruit recruit) {
        return RecruitClassInfo.builder()
                .ageClass(recruit.getAgeClass().getDescription())
                .recruitCnt(recruit.getRecruitCnt())
                .build();
    }


    @Schema(description = "어린이집의 클래스(분반) 정보 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KindergartenClassInfo {

        @Schema(description = "어린이집 이름", example = "새빛 어린이집")
        private String kindergartenName;

        @Schema(description = "어린이집의 분반 리스트")
        private List<RecruitClassInfo> classInfoList;

        public static KindergartenClassInfo createEmpty() {
            return KindergartenClassInfo.builder()
                    .kindergartenName(null)
                    .classInfoList(null)
                    .build();
        }
    }

    @Schema(description = "모집 생성 정보 DTO")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitCreationInfo {

        @Schema(description = "어린이집 및 분반 정보 리스트")
        private List<KindergartenClassInfo> kindergartenClassInfoList;

        @Schema(description = "모집 기간 상세 및 가중치 설정 정보")
        private RecruitDateAndWeightInfo recruitDateAndWeightInfo;

        @Schema(description = "이미 생성된 모집 여부")
        private Boolean isCreated;
    }



    public static RecruitCreationInfo toRecruitCreationInfo(List<KindergartenClassInfo> kindergartenClassInfos,
            RecruitDateAndWeightInfo recruitDetails, Boolean isCreated) {
        return RecruitCreationInfo.builder()
                .kindergartenClassInfoList(kindergartenClassInfos)  // 어린이집 및 분반 정보 리스트
                .recruitDateAndWeightInfo(recruitDetails) // 모집 기간 상세 및 가중치 설정 정보
                .isCreated(isCreated)
                .build();
    }

    public static RecruitCreationInfo toRecruitCreationInfo(List<KindergartenClassInfo> kindergartenClassInfos,
            RecruitDateAndWeightInfo recruitDetails) {
        return RecruitCreationInfo.builder()
                .kindergartenClassInfoList(kindergartenClassInfos)  // 어린이집 및 분반 정보 리스트
                .recruitDateAndWeightInfo(recruitDetails)  // 모집 기간 상세 및 가중치 설정 정보
                .build();
    }


}