package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.lottery.Lottery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Schema(description = "신청서 응답 DTO")
public class ApplicationResponse {

    private Long id; //신청서 id

    @JsonIgnore
    private Employee employee;

    private Character isSingleParent;
    private Integer ChildrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private Accept isAccept;
    private Save isTemp;
    private LocalDate joinDt;
    private List<Document> documents;

    public static ApplicationResponse toEntity(Application application) {

        Employee employee = application.getEmployee();

        return ApplicationResponse.builder()
                .id(application.getId())
                .employee(employee)
                .joinDt(employee.getJoinDt())
                .isSingleParent(application.getIsSingleParent())
                .ChildrenCnt(application.getChildrenCnt())
                .isDisability(application.getIsDisability())
                .isDualIncome(application.getIsDualIncome())
                .isEmployeeCouple(application.getIsEmployeeCouple())
                .isSibling(application.getIsSibling())
                .isAccept(application.getIsAccept())
                .isTemp(application.getIsTemp())
                .documents(application.getDocuments())
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ApplicationInfo {

        private Long id;
        @Schema(description = "한부모 가정 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "0")
        private Character isSingleParent;
        @Schema(description = "자녀 수 (해당되는 자녀 수 만큼 입력)", example = "3")
        private Integer ChildrenCnt;
        @Schema(description = "장애 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
        private Character isDisability;
        @Schema(description = "맞벌이 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
        private Character isDualIncome;
        @Schema(description = "사내 커플 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
        private Character isEmployeeCouple;
        @Schema(description = "형제 자매 재원 여부 (해당 시 '1', 미해당 시 '0'으로 입력)", example = "1")
        private Character isSibling;
        @Schema(description = "(어린이 : 모집ID) 리스트", example = """
                [
                    {
                      "childNm": "Ali",
                      "recruitIds": [1, 2]
                    },
                    {
                      "childNm": "Bob",
                      "recruitIds": [3, 4]
                    }
                  ]""")
        private List<ChildrenRecruit> childrenRecruitList;
        @Schema(description = "증빙 서류 URL 리스트", example = "{\n"
                + "      \"RESIDENT_REGISTER\": \"s3-1\",\n"
                + "      \"DUAL_INCOME\": \"s3-2\",\n"
                + "      \"SINGLE_PARENT\": \"s3-3\",\n"
                + "      \"DISABILITY\": \"s3-4\",\n"
                + "      \"MULTI_CHILDREN\": \"s3-5\",\n"
                + "      \"SIBLING\": \"s3-6\"\n"
                + "    }")
        private List<Document> documents;
    }

    public static ApplicationInfo toApplicationInfo(Application application,
            List<ChildrenRecruit> childrenRecruits) {

        return ApplicationInfo.builder()
                .id(application.getId())
                .isSingleParent(application.getIsSingleParent())
                .ChildrenCnt(application.getChildrenCnt())
                .isDisability(application.getIsDisability())
                .isDualIncome(application.getIsDualIncome())
                .isEmployeeCouple(application.getIsEmployeeCouple())
                .isSibling(application.getIsSibling())
                .childrenRecruitList(childrenRecruits)
                .documents(application.getDocuments())
                .build();
    }

    public static ApplicationInfo emptyApplicationInfo() {
        return new ApplicationInfo();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ChildrenRecruit {

        private String childNm;
        private List<Long> recruitIds;
    }

    public static List<ChildrenRecruit> toChildrenRecruits(List<Lottery> lotteries) {

        Map<String, List<Long>> groupByChildNm = lotteries.stream()
                .collect(Collectors.groupingBy(
                        Lottery::getChildNm, Collectors.mapping(
                                l -> l.getRecruit().getId(), Collectors.toList()
                        )
                ));

        List<ChildrenRecruit> childrenRecruits = new ArrayList<>();

        groupByChildNm.forEach((childNm, recruitIds) -> {
            childrenRecruits.add(ChildrenRecruit.builder()
                    .childNm(childNm)
                    .recruitIds(recruitIds)
                    .build());
        });

        return childrenRecruits;
    }

    public static ApplicationResponse emptyEntity() {
        return new ApplicationResponse();
    }
}