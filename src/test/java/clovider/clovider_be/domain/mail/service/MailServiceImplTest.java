package clovider.clovider_be.domain.mail.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.utils.dto.TestDto.VerifyCode;
import clovider.clovider_be.global.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private RedisUtil redisUtil;

    @Test
    void verifyAuthCode() {

        // given
        String authCode = "123456";
        VerifyCode verifyCode = new VerifyCode("qwe1234", "123456");

        // when
        when(redisUtil.getEmailCode(verifyCode.getAccountId())).thenReturn(authCode);
        String sucessString = mailService.verifyAuthCode(verifyCode);

        // then
        verify(redisUtil, times(1)).getEmailCode(verifyCode.getAccountId());
        assertThat(sucessString).isEqualTo("사내 이메일 인증에 성공하였습니다.");
        assertThat(verifyCode.getAuthCode()).isNotEmpty();
        assertThat(authCode).isEqualTo(verifyCode.getAuthCode());
        assertThat(verifyCode.getAuthCode()).hasSize(6);

    }

}