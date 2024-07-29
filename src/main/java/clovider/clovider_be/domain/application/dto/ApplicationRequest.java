package clovider.clovider_be.domain.application.dto;

import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ApplicationRequest {

    //workYears는 기존 사용자 정보로 입력
    private Character isSingleParent;
    private Integer childrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private String childName;

    private List<Recruit> recruitList;
    private List<String> imageUrls;
}