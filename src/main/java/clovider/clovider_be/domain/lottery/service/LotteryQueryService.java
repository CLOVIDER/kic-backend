package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotteryQueryService {

    List<CompetitionRate> getRecruitRates(List<Recruit> recruits);

    Long getTotalApplication(List<Recruit> recruits);

    Long getUnAcceptApplication(List<Recruit> recruits);

    List<AcceptResult> getAcceptStatus(List<Recruit> recruits);

    List<RecruitResult> getRecruitResult(Long recruitId);

    Page<Lottery> getNowLotteries(List<Recruit> recruits, Pageable pageable, SearchVO searchVO);

}
