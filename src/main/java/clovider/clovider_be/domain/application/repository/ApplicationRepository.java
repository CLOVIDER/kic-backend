package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

}

