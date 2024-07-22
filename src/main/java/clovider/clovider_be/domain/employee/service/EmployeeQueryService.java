package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.auth.dto.AuthRequest.LoginRequest;

public interface EmployeeQueryService {

    Employee getEmployee(Long id);

    Employee checkAccountIdAndPwd(LoginRequest loginRequest);
}
