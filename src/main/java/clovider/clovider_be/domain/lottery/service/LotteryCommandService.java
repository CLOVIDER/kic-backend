package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import java.util.List;

public interface LotteryCommandService {
    LotteryResponseDTO createLottery(Long recruitId);
    LotteryResisterResponseDTO updateRegistry(Long lotteryId);

    void insertLottery(List<Long> recruitIdList, Long applicationId);
    void deleteLottery(Long applicationId);
    Double getPercent(Long lotteryId);

    void deleteLotteryBylotteryId(Long lotteryId);
}
