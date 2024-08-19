package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.lottery.Lottery;
import java.util.List;

import clovider.clovider_be.domain.lottery.dto.LotteryIdAndChildNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LotteryRepository extends JpaRepository<Lottery, Long>, LotteryRepositoryCustom {

    @Query("select l.application from Lottery l where l.recruit.id = :recruitId and l.application.isAccept = 'ACCEPT'")
    List<Application> findAllApplicationByRecruitId(@Param("recruitId") Long recruitId);

    @Query("select l from Lottery l join fetch l.application join fetch l.application.employee " +
            "where l.recruit.id = :recruitId")
    List<Lottery> findAllByRecruitId(@Param("recruitId") Long recruitId);

    @Query("select l from Lottery l join fetch l.application join fetch l.application.employee " +
            "where l.recruit.id in :recruitIds")
    List<Lottery> findAllByRecruitIds(@Param("recruitIds") List<Long> recruitIds);

    List<Lottery> findAllByApplication(Application application);

    @Query("select l.id from Lottery l where l.application.id = :applicationId and l.recruit.id = :recruitId")
    Long findLotteryIdByApplication(@Param("applicationId") Long applicationId, @Param("recruitId") Long recruitId);


    @Query("select l.recruit.id from Lottery l where l.id = :lotteryId")
    Long findRecruitId(Long lotteryId);

    Lottery findLotteryByApplicationIdAndRecruitId(Long applicationId, Long recruitId);


    List<Lottery> findByApplicationId(Long applicationId);


    @Query("SELECT l.childNm AS childName, GROUP_CONCAT(l.id) AS lotteryIds " +
            "FROM Lottery l " +
            "JOIN l.application a " +
            "WHERE a.employee = :employee " +
            "AND a.id = ( " +
            "    SELECT MAX(sub_a.id) " +
            "    FROM Lottery sub_l " +
            "    JOIN sub_l.application sub_a " +
            "    WHERE sub_l.childNm = l.childNm " +
            ") " +
            "GROUP BY l.childNm")
    List<Object[]> findLotteryGroupedByChildNameByEmployee(Employee employee);
}

