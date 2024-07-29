package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepositoryCustom {

    List<CompetitionRate> findCompetitionRates(List<Recruit> recruits);

    Long findTotalApplication(List<Recruit> recruits);

    Long findUnAcceptApplication(List<Recruit> recruits);

    List<AcceptResult> findAcceptStatus(List<Recruit> recruits);

    Page<Lottery> findAllByRecruits(List<Recruit> recruits, Pageable pageable);
}
