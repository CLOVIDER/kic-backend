package clovider.clovider_be.domain.employee.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class EmployeeRequest {

    @Getter
    public static class ChangePassword {

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }
}
