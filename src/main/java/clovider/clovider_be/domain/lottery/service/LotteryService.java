package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;

public interface LotteryService {
    LotteryResponseDTO createLottery(Long recruitId, Long applicationId);
    LotteryResponseDTO updateRegistry(Long lotteryId);
}
