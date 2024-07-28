package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.recruit.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a JOIN Lottery l ON a.id = l.application.id WHERE l.recruit.id = :recruitId")
    List<Application> findAllByRecruitId(Long recruitId);

}

