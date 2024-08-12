package clovider.clovider_be.domain.recruit.dto;

import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruitResponseDTO {
    private boolean isSuccess;
    private String code;
    private String message;
    private Result result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private LocalDateTime createdAt;
    }
}
