package clovider.clovider_be.domain.employee.controller;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.dto.EmployeeRequest.ChangePassword;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse;
import clovider.clovider_be.domain.employee.dto.EmployeeResponse.EmployeeInfo;
import clovider.clovider_be.domain.employee.service.EmployeeCommandService;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee 관련 API 명세서", description = "비밀번호 변경 처리하는 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeCommandService employeeCommandService;
    private final EmployeeQueryService employeeQueryService;

    @Operation(summary = "(이메일 인증 코드 검증 후) 사내 직원의 비밀번호를 변경합니다.", description = "JWT을 Header에 담아서 요청해야합니다.")
    @PatchMapping("/employees")
    public ApiResponse<CustomResult> changePassword(@AuthEmployee Employee employee,
            @RequestBody @Valid ChangePassword request) {

        return ApiResponse.onSuccess(
                employeeCommandService.changePassword(employee, request.getPassword()));

    }

    @GetMapping("/employees")
    public ApiResponse<EmployeeInfo> getEmployeeInfo(@AuthEmployee Employee employee) {

        return ApiResponse.onSuccess(EmployeeResponse.toEmployeeInfo(employee));
    }

    @Operation(summary = "직원 정보 조회 - 관리자 신청 승인 페이지", description = "특정 신청서를 제출한 직원 정보를 조회합니다.")
    @Parameter(name = "applicationId", description = "신청서 ID", required = true, example = "1")
    @GetMapping("/admin/employees/{applicationId}")
    public ApiResponse<EmployeeInfo> getEmployeeInfoByApplicationId(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(employeeQueryService.getEmployeeInfoByApplicationId(applicationId));
    }



}
