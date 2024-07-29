package clovider.clovider_be.domain.mail.service;

import clovider.clovider_be.domain.employee.dto.EmployeeRequest.VerifyCode;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import jakarta.mail.MessagingException;
import java.util.List;

public interface MailService {


    void sendEmailMessage(String email) throws MessagingException;

    String verifyAuthCode(VerifyCode request);

    void sendRecruitResult(List<RecruitResult> results);
}
