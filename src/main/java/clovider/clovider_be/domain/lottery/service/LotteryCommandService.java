package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;

public interface LotteryCommandService {
    LotteryResponseDTO createLottery(Long recruitId, Long applicationId);
    LotteryResisterResponseDTO updateRegistry(Long lotteryId);


}
