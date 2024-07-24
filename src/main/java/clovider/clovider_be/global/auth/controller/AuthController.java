package clovider.clovider_be.global.auth.controller;

import static clovider.clovider_be.domain.common.CustomResult.toCustomResult;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeCommandService;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.global.auth.dto.TokenVo;
import clovider.clovider_be.global.auth.service.AuthService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    private final EmployeeCommandService employeeCommandService;
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenVo> login(@RequestBody LoginRequest request,
            HttpServletResponse response) {

        Employee employee = employeeQueryService.checkAccountIdAndPwd(request);
        TokenVo tokenVo = authService.generateATAndRT(employee);
        // authService.setHeaderAccessToken(response, tokenVo.accessToken());
        // authService.setHeaderRefreshToken(response, tokenVo.refreshToken());

        return ApiResponse.onSuccess(tokenVo);
    }

    @Operation(summary = "회원가입 - 직원 더미 데이터 생성하는 API", description = "비밀번호를 bcrypt 암호화 적용해서 DB에 저장하기")
    @PostMapping("/signup")
    public ApiResponse<CustomResult> singUp(@RequestBody LoginRequest request) {

        Employee employee = employeeCommandService.signUp(request);

        return ApiResponse.onSuccess(toCustomResult(employee.getId()));
    }
}
