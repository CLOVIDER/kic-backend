package clovider.clovider_be.domain.kindergartenClass.repository;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindergartenClassRepository extends JpaRepository<KindergartenClass, Long> {
    List<KindergartenClass> findByKindergartenId(Long kindergartenId);
    void deleteAllByKindergartenId(Long kindergartenId);
}
