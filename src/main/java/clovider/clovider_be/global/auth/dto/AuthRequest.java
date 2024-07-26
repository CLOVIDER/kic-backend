package clovider.clovider_be.global.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


public class AuthRequest {

    @Getter
    public static class LoginRequest {

        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }

}
