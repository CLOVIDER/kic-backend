package clovider.clovider_be.domain.employee.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeRequest.ChangePassword;
import clovider.clovider_be.domain.employee.service.EmployeeCommandService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee 관련 API 명세서", description = "비밀번호 변경 처리하는 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeCommandService employeeCommandService;

    @Operation(summary = "(이메일 인증 코드 검증 후) 사내 직원의 비밀번호를 변경합니다.", description = "JWT을 Header에 담아서 요청해야합니다.")
    @PatchMapping("/employees")
    public ApiResponse<CustomResult> changePassword(@AuthEmployee Employee employee,
            @RequestBody @Valid ChangePassword request) {

        return ApiResponse.onSuccess(
                employeeCommandService.changePassword(employee, request.getPassword()));

    }

}
