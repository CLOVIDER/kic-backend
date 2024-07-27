package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.TotalApplication;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;

public interface LotteryQueryService {

    List<CompetitionRate> getRecruitRates(List<Recruit> recruits);

    List<TotalApplication> getTotalApplication(List<Recruit> recruits);

    List<TotalApplication> getUnAcceptApplication(List<Recruit> recruits);

    List<AcceptResult> getAcceptStatus(List<Recruit> recruits);

}
