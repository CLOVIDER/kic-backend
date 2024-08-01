package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import java.util.List;
import java.util.Map;

public interface LotteryCommandService {
    LotteryResponseDTO createLottery(Long recruitId);
    LotteryResisterResponseDTO updateRegistry(Long lotteryId);

    void insertLottery(List<Map<String, Object>> childrenRecruitList, Long applicationId);
    void deleteLottery(Long applicationId);
    Double getPercent(Long lotteryId);

    void deleteLotteryBylotteryId(Long lotteryId);
}
