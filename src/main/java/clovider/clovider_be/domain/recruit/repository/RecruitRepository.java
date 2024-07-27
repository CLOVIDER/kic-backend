package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.recruit.Recruit;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    List<Recruit> findByKindergartenId(Long kindergartenId);

    @Query("SELECT r FROM Recruit r JOIN FETCH r.kindergarten k WHERE r.recruitStartDt <= :now AND r.recruitEndDt >= :now")
    List<Recruit> findNowRecruit(@Param("now") LocalDateTime now);
}

