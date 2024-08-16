package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
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

    @Query("select r from Recruit r join fetch r.kindergarten where r.recruitStartDt <= :now and r.recruitEndDt >= :now order by r.ageClass")
    List<Recruit> findRecruitKdg(LocalDateTime now);

    @Query("select r from Recruit r join fetch r.kindergarten where (r.firstEndDt <= :now and r.secondStartDt >= :now) or (r.secondEndDt <= :now and r.recruitEndDt >= :now) order by r.ageClass")
    List<Recruit> findRecruitResult(LocalDateTime now);

    @Query("select r from Recruit r join fetch r.kindergarten k where r.id = :recruitId")
    Optional<Recruit> findRecruitInfoById(@Param("recruitId") Long recruitId);

    @Query("select r.kindergarten.id from Recruit r where r.id = :recruitId")
    Long findKindergartenIdByRecruitId(@Param("recruitId") Long recruitId);

    @Query("select r.ageClass from Recruit r where r.id = :recruitId")
    int finAgeClassById(@Param("recruitId") Long recruitId);

    Optional<Recruit> findByKindergartenAndAgeClass(Kindergarten kindergarten, Integer ageClass);
}
