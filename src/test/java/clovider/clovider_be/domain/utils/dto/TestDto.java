package clovider.clovider_be.domain.utils.dto;

import clovider.clovider_be.domain.auth.dto.AuthRequest;
import clovider.clovider_be.domain.employee.dto.EmployeeRequest;

public class TestDto {

    public static class LoginRequest extends AuthRequest.LoginRequest {

        private String accountId;

        private String password;

        public LoginRequest(String accountId, String password) {
            this.accountId = accountId;
            this.password = password;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getPassword() {
            return password;
        }
    }


    public static class VerifyCode extends EmployeeRequest.VerifyCode {

        private String accountId;

        private String authCode;

        public VerifyCode(String accountId, String authCode) {
            this.accountId = accountId;
            this.authCode = authCode;
        }

        public String getAccountId() {
            return accountId;
        }

        public String getAuthCode() {
            return authCode;
        }
    }

}
