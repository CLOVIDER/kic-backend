package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse.EmployeeInfo;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.domain.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
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
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    // TODO: BCrypt 적용 부분 추가하기 (Before: 회원가입으로 더미데이터 넣기)
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
    public EmployeeInfo getEmployeeInfoByApplicationId(Long applicationId) {
        Application application = applicationQueryService.getApplication(applicationId);
        return EmployeeResponse.toEmployeeInfo(application.getEmployee());
    }

}
