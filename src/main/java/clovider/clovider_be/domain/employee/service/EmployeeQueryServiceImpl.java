package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND));
    }
}
