package clovider.clovider_be.domain.employee.repository;

import clovider.clovider_be.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
