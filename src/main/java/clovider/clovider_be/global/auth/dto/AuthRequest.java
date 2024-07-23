package clovider.clovider_be.global.auth.dto;

import lombok.Getter;


public class AuthRequest {

    @Getter
    public static class LoginRequest {
        private String accountId;
        private String password;
    }

}
