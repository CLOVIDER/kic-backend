package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepositoryCustom {

    List<CompetitionRate> findCompetitionRates(List<Recruit> recruits);
}
