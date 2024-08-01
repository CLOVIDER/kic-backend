package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse.EmployeeInfo;

public interface EmployeeQueryService {

    Employee getEmployee(Long id);

    Employee checkAccountIdAndPwd(LoginRequest loginRequest);

    EmployeeInfo getEmployeeInfo(Long applicationId);

}
