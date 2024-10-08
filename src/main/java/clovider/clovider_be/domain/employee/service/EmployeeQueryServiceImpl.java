package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse.EmployeeInfo;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeRepository employeeRepository;
    private final ApplicationQueryService applicationQueryService;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    @Override
    public Employee findEmployee(Employee employee) {
        return employeeRepository.findById(employee.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    @Override
    public Employee checkAccountIdAndPwd(LoginRequest loginRequest) {

        Employee employee = employeeRepository.findByAccountId(
                loginRequest.getAccountId()).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
        if (!bCryptEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            throw new ApiException(ErrorStatus._AUTH_INVALID_PASSWORD);
        }

        return employee;
    }

    @Override
    public EmployeeInfo getEmployeeInfo(Long applicationId) {
        Application application = applicationQueryService.getApplicationWithEmployee(applicationId);
        return EmployeeResponse.toEmployeeInfo(application.getEmployee());
    }

}
