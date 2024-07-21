package clovider.clovider_be.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotteryCreateRequestDTO {
    private Long applicationId;
    private Integer rankNm;
    private String result;
    private Boolean registry;
    private Boolean accept;
}

