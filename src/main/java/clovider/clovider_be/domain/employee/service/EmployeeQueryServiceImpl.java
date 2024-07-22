package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.jwt.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    // TODO: BCrypt 적용 부분 추가하기 (Before: 회원가입으로 더미데이터 넣기)
    @Override
    public Employee checkAccountIdAndPwd(LoginRequest loginRequest) {
        return employeeRepository.findByAccountIdAndPassword(
                loginRequest.getAccountId(),
                loginRequest.getPassword()).orElseThrow(
                () -> new ApiException(ErrorStatus._JWT_LOGIN_ERROR)
        );
    }
}
