package clovider.clovider_be.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.auth.dto.TokenVo;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.utils.CreateUtil;
import clovider.clovider_be.global.jwt.JwtProvider;
import clovider.clovider_be.global.util.RedisUtil;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("AT & RT")
    void generateATAndRT() throws NoSuchFieldException, IllegalAccessException {

        // given
        List<Employee> employeeList = CreateUtil.getEmployeeList();
        Employee employee = employeeList.get(0);
        String accessToken = "accesstokenaccesstoken";
        String refreshToken = "refreshtokenrefreshtoken";
        Long expiration = 3600L;

        // when
        when(jwtProvider.generateAccessToken(anyLong(), any())).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(anyLong(), any())).thenReturn(refreshToken);
        when(jwtProvider.getExpiration(anyString())).thenReturn(expiration);

        TokenVo tokenVo = authService.generateATAndRT(employee);

        // then
        assertThat(accessToken).isEqualTo(tokenVo.accessToken());
    }

    @Test
    @DisplayName("Refresh Token 재발급")
    void reIssueToken() throws NoSuchFieldException, IllegalAccessException {

        // given
        List<Employee> employeeList = CreateUtil.getEmployeeList();
        Employee employee = employeeList.get(0);
        String accessToken = "accesstokenaccesstoken";
        String refreshToken = "refreshtokenrefreshtoken";
        Long expiration = 3600L;

        // when
        when(jwtProvider.getEmployeeId(anyString())).thenReturn(1L);
        when(jwtProvider.getRoleByToken(anyString())).thenReturn(Role.EMPLOYEE);
        when(jwtProvider.generateAccessToken(anyLong(), any())).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(anyLong(), any())).thenReturn(refreshToken);
        when(jwtProvider.getExpiration(anyString())).thenReturn(expiration);

        TokenVo tokenVo = authService.reIssueToken(refreshToken);

        // then
        assertThat(accessToken).isEqualTo(tokenVo.accessToken());
        assertThat(refreshToken).isEqualTo(tokenVo.refreshToken());
        assertThat(employee.getRole().toString()).isEqualTo(tokenVo.role());

    }
}