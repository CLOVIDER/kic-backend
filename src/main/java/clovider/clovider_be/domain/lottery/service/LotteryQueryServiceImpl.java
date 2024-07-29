package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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


    public List<CompetitionRate> getRecruitRates(List<Recruit> recruits) {

        return lotteryRepository.findCompetitionRates(recruits);
    }

    @Override
    public Long getTotalApplication(List<Recruit> recruits) {
        return lotteryRepository.findTotalApplication(recruits);
    }

    @Override
    public Long getUnAcceptApplication(List<Recruit> recruits) {
        return lotteryRepository.findUnAcceptApplication(recruits);
    }

    @Override
    public List<AcceptResult> getAcceptStatus(List<Recruit> recruits) {
        return lotteryRepository.findAcceptStatus(recruits);
    }

    @Override
    public List<RecruitResult> getRecruitResult(Long recruitId) {

        return LotteryResponse.toRecruitResults(lotteryRepository.findAllByRecruitId(recruitId));
    }
}
