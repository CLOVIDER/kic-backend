package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepositoryCustom {

    List<CompetitionRate> findCompetitionRates(List<Recruit> recruits);

    Long findTotalApplication(List<Recruit> recruits);

    Long findUnAcceptApplication(List<Recruit> recruits);

    List<AcceptResult> findAcceptStatus(List<Recruit> recruits);

    List<Long> findApplicationsAllByRecruits(List<Recruit> recruits);

    Page<LotteryResult> getLotteryResults(Long kindergartenId, Pageable pageable, String value);
}
