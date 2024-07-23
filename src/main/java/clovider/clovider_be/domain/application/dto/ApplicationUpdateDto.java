package clovider.clovider_be.domain.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplicationUpdateDto {

    private Character isSingleParent;
    private Integer childrenCnt;
    private Character isDisability;
    private Character isDualIncome;
    private Character isEmployeeCouple;
    private Character isSibling;
    private String childName;

    private List<String> imageUrls;
}