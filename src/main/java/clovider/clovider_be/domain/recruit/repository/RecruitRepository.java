package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.recruit.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}

