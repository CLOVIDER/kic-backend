package clovider.clovider_be.domain.mail.service;

import clovider.clovider_be.domain.employee.dto.EmployeeRequest.VerifyCode;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final RedisUtil redisUtil;
    private static final String GACHON = "@gachon.ac.kr";

    @Override
    public void sendEmailMessage(String accountId) throws MessagingException {

        String authCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        String email = accountId + GACHON;
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("[KIDS IN COMPANY] 사내 이메일 인증 - 인증 코드 발송");
        message.setText(setContext(authCode), "UTF-8", "html");
        javaMailSender.send(message);

        redisUtil.setEmailCode(accountId, authCode);
    }

    @Override
    public String verifyAuthCode(VerifyCode request) {

        String authCode = redisUtil.getEmailCode(request.getAccountId());

        return Optional.ofNullable(authCode)
                .filter(code -> code.equals(request.getAuthCode()))
                .map(code -> "사내 이메일 인증에 성공하였습니다.")
                .orElseThrow(() -> new ApiException(ErrorStatus._MAIL_WRONG_CODE));
    }

    private String createCode() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            int authCode = random.nextInt(1000000);
            log.info("===================== authCode: " + authCode);
            return String.format("%06d", authCode);
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ErrorStatus._MAIL_CREATE_CODE_ERROR);
        }
    }

    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mailCode", context);
    }
}
