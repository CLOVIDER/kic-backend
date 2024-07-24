package clovider.clovider_be.domain.mail.service;

import clovider.clovider_be.domain.employee.dto.EmployeeRequest.VerifyCode;
import jakarta.mail.MessagingException;

public interface MailService {


    void sendEmailMessage(String email) throws MessagingException;

    String verifyAuthCode(VerifyCode request);
}
