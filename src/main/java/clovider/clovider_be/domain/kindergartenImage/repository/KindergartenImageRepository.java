package clovider.clovider_be.domain.kindergartenImage.repository;

import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindergartenImageRepository extends JpaRepository<KindergartenImage, Long> {
    List<KindergartenImage> findByKindergartenId(Long kindergartenId);
}
