package clovider.clovider_be.global.jwt.controller;

import static clovider.clovider_be.domain.common.CustomResult.toCustomResult;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.jwt.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.global.jwt.dto.TokenVo;
import clovider.clovider_be.global.jwt.service.AuthService;
import clovider.clovider_be.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final EmployeeQueryService employeeQueryService;
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<CustomResult> login(@RequestBody LoginRequest request,
            HttpServletResponse response) {

        Employee employee = employeeQueryService.checkAccountIdAndPwd(request);
        TokenVo tokenVo = authService.generateATAndRT(employee);
        authService.setHeaderAccessToken(response, tokenVo.accessToken());
        authService.setHeaderRefreshToken(response, tokenVo.refreshToken());

        return ApiResponse.onSuccess(toCustomResult(employee.getId()));
    }
}
