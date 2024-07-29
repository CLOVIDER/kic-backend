package clovider.clovider_be.domain.lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryResponseDTO {
    private boolean isSuccess;
    private String code;
    private String message;
    private Result result;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private Long id;
        private LocalDateTime createdAt;
    }
}
