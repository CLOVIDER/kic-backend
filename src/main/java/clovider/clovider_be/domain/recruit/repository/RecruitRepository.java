package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.recruit.Recruit;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {

    List<Recruit> findByKindergartenId(Long kindergartenId);

    @Query("select r from Recruit r where r.recruitStartDt <= :now and r.recruitEndDt >= :now")
    List<Recruit> findNowRecruit(@Param("now") LocalDateTime now);

    @Query("select r from Recruit r join fetch r.kindergarten k where r.id = :recruitId")
    Optional<Recruit> findRecruitInfoById(@Param("recruitId") Long recruitId);

}

