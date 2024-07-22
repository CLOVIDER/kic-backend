package clovider.clovider_be.domain.employee.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;

public interface EmployeeCommandService {

    CustomResult changePassword(Employee employee, String newPassword);

}
