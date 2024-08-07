package clovider.clovider_be.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryResultResponseDTO {
    private Result result;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        //추첨 id
        private Long id;
        private LocalDateTime createdAt;
        //추첨 결과
        private clovider.clovider_be.domain.enums.Result result;
        // 해당 어린이집
        String kindergartenNm;
    }
}
