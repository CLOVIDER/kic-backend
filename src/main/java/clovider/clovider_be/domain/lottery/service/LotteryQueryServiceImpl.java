package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Lottery> getNowLotteries(List<Recruit> recruits, Pageable pageable) {

        return lotteryRepository.findAllByRecruits(recruits, pageable);
    }
}
