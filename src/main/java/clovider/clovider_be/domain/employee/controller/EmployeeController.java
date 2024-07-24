package clovider.clovider_be.domain.employee.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeRequest.ChangePassword;
import clovider.clovider_be.domain.employee.service.EmployeeCommandService;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeQueryService employeeQueryService;
    private final EmployeeCommandService employeeCommandService;

    @PatchMapping("/employees")
    public ApiResponse<CustomResult> changePassword(@RequestBody @Valid ChangePassword request) {

        // TODO : @AuthEmployee로 변경
        Employee employee = employeeQueryService.getEmployee(1L);

        return ApiResponse.onSuccess(
                employeeCommandService.changePassword(employee, request.getPassword()));

    }

}
