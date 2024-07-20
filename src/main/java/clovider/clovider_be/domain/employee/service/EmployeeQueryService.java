package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.employee.Employee;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeQueryService {

    Employee findById(Long id);
}
