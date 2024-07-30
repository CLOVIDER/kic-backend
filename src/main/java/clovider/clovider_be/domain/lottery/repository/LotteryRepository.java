package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.lottery.Lottery;
import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LotteryRepository extends JpaRepository<Lottery, Long>, LotteryRepositoryCustom {

    List<Application> findApplicationByRecruitId(Long recruitId);
    
    @Query("select l from Lottery l join fetch l.application join fetch l.application.employee " +
            "where l.recruit.id = :recruitId")
    List<Lottery> findAllByRecruitId(@Param("recruitId") Long recruitId);

    List<Lottery> findAllByApplication(Application application);
}