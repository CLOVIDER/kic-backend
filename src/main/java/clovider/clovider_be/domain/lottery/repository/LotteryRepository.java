package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.lottery.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {
    List<Application> findByRecruitId(Long recruitId);

    List<Application> findAllByRecruitId(Long recruitId);

}
