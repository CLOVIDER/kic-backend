package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    }
