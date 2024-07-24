package clovider.clovider_be.domain.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
}
