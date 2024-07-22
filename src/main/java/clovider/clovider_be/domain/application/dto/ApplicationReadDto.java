package clovider.clovider_be.domain.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
public class ApplicationReadDto {

    private Long id; //신청서 id

    // 직원 도메인 구현 후 사용
//    private Long employeeId; //직원 id
//    private String accountId; //직원 사내 이메일에 사용하는 아이디

    // 모집 도메인 구현 후 사용
    // 신청서 하나 당 여러 모집에 해당되고 결과도 여러개니 List로 저장
//    private List<String> lotResultList;
//    private List<Integer> rankNoList;


    // 어린이집, 모집 도메인 구현 후 사용
    // (신청서 - 추첨 테이블끼리 조인 -> 추첨 테이블 내 모집ID 를 사용해서 모집 테이블에서 어린이집 ID 얻어옴 -> 어린이집 이름 가져옴)
    // 신청서 하나 당 여러 어린이집이 해당되니 List로 저장
//    private List<String> kdgList;

    private Integer workYears;
    private Boolean isSingleParent;
    private Integer isChildrenCnt;
    private Boolean isDisability;
    private Boolean isDualIncome;
    private Boolean isEmployeeCouple;
    private Boolean isSibling;
    private String childName;

    private List<MultipartFile> applicationDocumentList;
}