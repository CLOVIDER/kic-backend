package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.auth.dto.AuthRequest.LoginRequest;

public interface EmployeeCommandService {

    CustomResult changePassword(Employee employee, String newPassword);

    Employee signUp(LoginRequest request);

}
