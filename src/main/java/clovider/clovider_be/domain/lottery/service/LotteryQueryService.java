package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;

public interface LotteryQueryService {
    LotteryResultResponseDTO getLotteryResult(Long lotteryId);
}
