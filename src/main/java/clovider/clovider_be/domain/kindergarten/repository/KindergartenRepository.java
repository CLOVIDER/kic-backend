package clovider.clovider_be.domain.kindergarten.repository;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindergartenRepository extends JpaRepository<Kindergarten, Long> {
    Kindergarten findByKindergartenNm(String kindergartenNm);
}
