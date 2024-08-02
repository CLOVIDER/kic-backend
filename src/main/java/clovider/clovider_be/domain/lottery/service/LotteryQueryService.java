package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;

public interface LotteryQueryService {

    LotteryResultResponseDTO getLotteryResult(Long lotteryId);

    List<CompetitionRate> getRecruitRates(List<Recruit> recruits);

    Long getTotalApplication(List<Recruit> recruits);

    Long getUnAcceptApplication(List<Recruit> recruits);

    List<AcceptResult> getAcceptResult(List<Recruit> recruits);

    List<RecruitResult> getRecruitResult(Long recruitId);

    List<Long> getApplicationsByLotteries(List<Recruit> recruits);

}
