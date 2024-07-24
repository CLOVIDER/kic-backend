package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeRequest;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final EmployeeQueryService employeeQueryService;
    private final EmployeeRepository employeeRepository;

    @Override
    public CustomResult changePassword(Employee employee, String newPassword) {

        String encodePassword = bCryptEncoder.encode(newPassword);
        employee.changePassword(encodePassword);

        return CustomResult.toCustomResult(employee.getId());
    }

    @Override
    public Employee signUp(LoginRequest request) {

        if (employeeQueryService.existsEmployee(request.getAccountId())) {
            throw new ApiException(ErrorStatus._EMPLOYEE_DUPLICATED_ID);
        }

        Employee employee = EmployeeRequest.toEntity(request.getAccountId(),
                bCryptEncoder.encode(request.getPassword()));

        return employeeRepository.save(employee);
    }
}
