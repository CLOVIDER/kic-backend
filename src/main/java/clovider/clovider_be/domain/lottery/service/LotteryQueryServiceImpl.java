package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LotteryQueryServiceImpl implements LotteryQueryService {
    private final LotteryRepository lotteryRepository;

    @Override
    public LotteryResultResponseDTO getLotteryResult(Long lotteryId) {
        Lottery lottery = lotteryRepository.findById(lotteryId)
                .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));


        return new LotteryResultResponseDTO(
                "추첨 결과가 성공적으로 조회되었습니다.",
                new LotteryResultResponseDTO.Result(lottery.getId(), lottery.getCreatedAt(), lottery.getResult())
        );
    }
}
