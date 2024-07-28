package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
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
    public List<CompetitionRate> getRecruitRates(List<Recruit> recruits) {

        return lotteryRepository.findCompetitionRates(recruits);
    }

    @Override
    public List<RecruitResult> getRecruitResult(Long recruitId) {

        return LotteryResponse.toRecruitResults(lotteryRepository.findAllByRecruitId(recruitId));
    }
}
