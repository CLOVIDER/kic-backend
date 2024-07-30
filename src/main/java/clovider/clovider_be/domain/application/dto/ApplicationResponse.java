package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.lottery.Lottery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ApplicationResponse {

    private Long id; //신청서 id

    @JsonIgnore
    private Employee employee;

//     모집 도메인 구현 후 사용
//     신청서 하나 당 여러 모집에 해당되고 결과도 여러개니 List로 저장
//    private List<Result> ResultList;
//    private List<Integer> rankNoList;

    // 어린이집, 모집 도메인 구현 후 사용
    // (신청서 - 추첨 테이블끼리 조인 -> 추첨 테이블 내 모집ID 를 사용해서 모집 테이블에서 어린이집 ID 얻어옴 -> 어린이집 이름 가져옴)
    // 신청서 하나 당 여러 어린이집이 해당되니 List로 저장
//    private List<String> kdgList;

    private Integer workYears;
    private Character isSingleParent;
    private Integer isChildrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private String childNm;

    private List<Document> documents;

    public static ApplicationResponse toEntity(Application application) {

        return ApplicationResponse.builder()
                .id(application.getId())
                .employee(application.getEmployee())
                .workYears(application.getWorkYears())
                .isSingleParent(application.getIsSingleParent())
                .isChildrenCnt(application.getChildrenCnt())
                .isDisability(application.getIsDisability())
                .isDualIncome(application.getIsDualIncome())
                .isEmployeeCouple(application.getIsEmployeeCouple())
                .isSibling(application.getIsSibling())
                .childNm(application.getChildNm())
                .documents(application.getDocuments())
                .build();
    }
}