package clovider.clovider_be.domain.auth.service;

import clovider.clovider_be.domain.auth.dto.TokenVo;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.jwt.JwtProvider;
import clovider.clovider_be.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        return new TokenVo(accessToken, refreshToken, employee.getRole().toString());
    }

}
