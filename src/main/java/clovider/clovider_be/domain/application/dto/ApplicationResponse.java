package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.DocumentType;
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
        private Integer childrenCnt;
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
        private List<DocumentInfo> documents;
    }

    public static ApplicationInfo toApplicationInfo(Application application,
            List<ChildrenRecruit> childrenRecruits) {

        List<DocumentInfo> documentInfos = application.getDocuments().stream()
                .map(ApplicationResponse::toDocumentInfo)
                .toList();

        return ApplicationInfo.builder()
                .id(application.getId())
                .isSingleParent(application.getIsSingleParent())
                .childrenCnt(application.getChildrenCnt())
                .isDisability(application.getIsDisability())
                .isDualIncome(application.getIsDualIncome())
                .isEmployeeCouple(application.getIsEmployeeCouple())
                .isSibling(application.getIsSibling())
                .childrenRecruitList(childrenRecruits)
                .documents(documentInfos)
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class DocumentInfo {

        @Schema(description = "Document ID", example = "1")
        private Long id;
        @Schema(description = "증빙서류 S3 경로", example = "https://kidsincompany-bucket.s3.ap-northeast-2.amazonaws.com/images/kindergarten/08b4d564-68d7-4061-b8da-823f0acfc287.jpg")
        private String image;
        @Schema(description = "문서 종류 / 주민등록등본, 맞벌이 여부, 한부모 가정, 장애 증빙, 다자녀, 형제/자매", example = "SINGLE_PARENT")
        private DocumentType documentType;
    }

    public static DocumentInfo toDocumentInfo(Document document) {

        return DocumentInfo.builder()
                .id(document.getId())
                .image(document.getImage())
                .documentType(document.getDocumentType())
                .build();
    }

    public static ApplicationResponse emptyEntity() {
        return new ApplicationResponse();
    }
}