package clovider.clovider_be.domain.employee.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;

public class EmployeeRequest {

    @Schema(description = "비밀번호 변경 요청 DTO")
    @Getter
    public static class ChangePassword {

        @Schema(description = "사내 직원의 비밀번호", example = "clovider")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }

    @Schema(description = "사내 이메일 인증 요청 DTO")
    @Getter
    public static class AuthAccountId {

        @Schema(description = "사내 직원의 아이디", example = "clovider1")
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;
    }

    @Schema(description = "사내 이메일 인증 코드 검증 DTO")
    @Getter
    public static class VerifyCode {

        @Schema(description = "사내 직원의 아이디", example = "clovider1")
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;

        @Schema(description = "이메일로부터 받은 인증 코드", example = "123456")
        @Pattern(regexp = "\\d{6}", message = "인증 코드는 6자리 숫자여야 합니다.")
        private String authCode;
    }

    public static Employee toEntity(String accountId, String password) {

        return Employee.builder()
                .nameKo("EMPLOYEE_1")
                .accountId(accountId)
                .password(password)
                .employeeNo("12345678")
                .joinDt(LocalDate.of(2024, 7, 1))
                .dept("IT")
                .role(Role.EMPLOYEE)
                .build();
    }
}
