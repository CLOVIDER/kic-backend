package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long>,
        ApplicationRepositoryCustom {

    Optional<Application> findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);

    @Query("SELECT a FROM Application a JOIN FETCH a.employee WHERE a.id = :applicationId")
    Application findApplicationWithEmployee(Long applicationId);
}