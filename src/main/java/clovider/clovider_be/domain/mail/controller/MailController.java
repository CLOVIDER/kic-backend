package clovider.clovider_be.domain.mail.controller;

import clovider.clovider_be.domain.employee.dto.EmployeeRequest.AuthAccountId;
import clovider.clovider_be.domain.mail.service.MailService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email 관련 API 명세서")
@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @Operation(summary = "사내 이메일 인증 코드 전송", description = "비밀번호 변경 시 이메일 인증 단계가 필요함 / 인증코드 숫자 6자리를 전송합니다.")
    @PostMapping
    public ApiResponse<String> sendAuthCode(@RequestBody AuthAccountId request)
            throws MessagingException {

        mailService.sendEmailMessage(request.getAccountId());

        return ApiResponse.onSuccess("성공적으로 인증코드가 전송되었습니다.");
    }
}
