package clovider.clovider_be.domain.auth.controller;

import static clovider.clovider_be.domain.common.CustomResult.toCustomResult;

import clovider.clovider_be.domain.auth.dto.AuthRequest.LoginRequest;
import clovider.clovider_be.domain.auth.dto.TokenVo;
import clovider.clovider_be.domain.auth.service.AuthService;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeCommandService;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth 관련 API 명세서", description = "로그인, 로그아웃(블랙리스트 관리), 토큰 갱신 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final EmployeeQueryService employeeQueryService;
    private final EmployeeCommandService employeeCommandService;
    private final AuthService authService;

    @Operation(summary = "사용자/관리자 로그인 API", description = "일반 직원인 경우 EMPLOYEE, 관리자인 경우 ADMIN을 반환합니다.")
    @PostMapping("/login")
    public ApiResponse<TokenVo> login(@RequestBody LoginRequest request) {

        Employee employee = employeeQueryService.checkAccountIdAndPwd(request);
        TokenVo tokenVo = authService.generateATAndRT(employee);

        return ApiResponse.onSuccess(tokenVo);
    }

    @Operation(summary = "로그아웃 및 블랙리스트 관리 API", description = "보안을 위해 로그아웃 시 AccessToken을 블랙리스트에 관리합니다.")
    @PostMapping("/logout")
    public ApiResponse<String> logout(@AuthEmployee Employee employee, HttpServletRequest request) {

        authService.logout(request);

        return ApiResponse.onSuccess("성공적으로 로그아웃되었습니다.");
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenVo> reissueToken(@RequestBody String refreshToken) {

        return ApiResponse.onSuccess(authService.reIssueToken(refreshToken));
    }

    @Operation(summary = "회원가입 - 직원 더미 데이터 생성하는 API", description = "비밀번호를 bcrypt 암호화 적용해서 DB에 저장하기")
    @PostMapping("/signup")
    public ApiResponse<CustomResult> singUp(@RequestBody LoginRequest request) {

        Employee employee = employeeCommandService.signUp(request);

        return ApiResponse.onSuccess(toCustomResult(employee.getId()));
    }
}
