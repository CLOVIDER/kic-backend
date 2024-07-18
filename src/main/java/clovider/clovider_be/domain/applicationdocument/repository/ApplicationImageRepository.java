package clovider.clovider_be.domain.applicationdocument.repository;

import clovider.clovider_be.domain.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationImageRepository extends JpaRepository<Application, Long> {

}
