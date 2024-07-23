package clovider.clovider_be.global.auth.service;

import static clovider.clovider_be.global.util.JwtProperties.ACCESS_HEADER_STRING;
import static clovider.clovider_be.global.util.JwtProperties.REFRESH_HEADER_STRING;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.jwt.JwtProvider;
import clovider.clovider_be.global.auth.dto.TokenVo;
import clovider.clovider_be.global.security.CustomUserDetails;
import clovider.clovider_be.global.util.JwtProperties;
import clovider.clovider_be.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    public TokenVo generateATAndRT(Employee employee) {
        String accessToken = jwtProvider.generateAccessToken(employee.getId(), employee.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(employee.getId());
        Long expiration = jwtProvider.getExpiration(refreshToken);

        log.info("===================== Add RefreshToken In Redis");
        redisUtil.set(employee.getId().toString(), refreshToken, expiration);

        return new TokenVo(accessToken, refreshToken);
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        String beaerToken = JwtProperties.TOKEN_PREFIX + accessToken;
        response.setHeader(ACCESS_HEADER_STRING, beaerToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_HEADER_STRING, refreshToken);
    }

    public Employee getCurrentEmployee() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return userDetails.getEmployee();
    }

}
