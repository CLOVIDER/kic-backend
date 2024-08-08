package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepositoryCustom {

    List<CompetitionRate> findCompetitionRates(List<Long> recruitIds);

    Long findTotalApplication(List<Long> recruitIds);

    Long findUnAcceptApplication(List<Long> recruitIds);

    List<AcceptResult> findAcceptStatus(List<Long> recruitIds);

    List<Long> findApplicationsAllByRecruits(List<Recruit> recruits);

    Page<LotteryResult> getLotteryResults(Long kindergartenId, Pageable pageable, String value);
}
