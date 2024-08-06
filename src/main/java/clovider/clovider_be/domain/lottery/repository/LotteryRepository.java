package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.lottery.Lottery;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotteryRepository extends JpaRepository<Lottery, Long>, LotteryRepositoryCustom {

    //List<Application> findApplicationByRecruitId(Long recruitId);

    @Query("select l.application from Lottery l where l.recruit.id = :recruitId")
    List<Application> findAllApplicationByRecruitId(@Param("recruitId") Long recruitId);

    @Query("select l from Lottery l join fetch l.application join fetch l.application.employee " +
            "where l.recruit.id = :recruitId")
    List<Lottery> findAllByRecruitId(@Param("recruitId") Long recruitId);

    List<Lottery> findAllByApplication(Application application);

    @Query("select l.id from Lottery l where l.application.id = :applicationId")
    Long findLotteryIdByApplication(@Param("applicationId") Long applicationId);


    Lottery findLotteryByApplicationIdAndRecruitId(Long applicationId, Long recruitId);



    List<Lottery> findByApplicationId(Long applicationId);
}