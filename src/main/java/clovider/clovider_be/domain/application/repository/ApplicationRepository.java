package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<Application, Long>,
        ApplicationRepositoryCustom {

    Optional<Application> findFirstByEmployeeOrderByCreatedAtDesc(Employee employee);

    @Query("select a from Application a join fetch a.employee where a.id = :applicationId")
    Application findApplicationWithEmployee(Long applicationId);

    List<Application> findAllByEmployeeId(Long employeeId);
}