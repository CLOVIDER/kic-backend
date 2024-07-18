package clovider.clovider_be.domain.kindergarden.repository;

import clovider.clovider_be.domain.kindergarden.Kindergarden;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KindergardenRepository extends JpaRepository<Kindergarden, Long> {
    Optional<Kindergarden> findById(Long id);
}
