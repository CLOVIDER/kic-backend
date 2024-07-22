package clovider.clovider_be.global.jwt;

import static clovider.clovider_be.global.util.JwtProperties.ACCESS_HEADER_STRING;
import static clovider.clovider_be.global.util.JwtProperties.REFRESH_HEADER_STRING;
import static clovider.clovider_be.global.util.JwtProperties.TOKEN_PREFIX;

import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.global.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (isPublicUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;

        String bearerToken = request.getHeader(ACCESS_HEADER_STRING);
        if (bearerToken == null || !isBearer(bearerToken)) {
            request.setAttribute("exception", ErrorStatus._JWT_NOT_FOUND);
            filterChain.doFilter(request, response);
            return;
        } else {
            accessToken = bearerToken.substring(7);
            log.info("===================== ACCESS-TOKEN : " + accessToken);

            if (checkBlackList(accessToken)) {
                log.info("===================== BLACKLIST LOGIN");
                throw new ApiException(ErrorStatus._JWT_BLACKLIST);
            }
        }

        if (jwtProvider.validateToken(accessToken)) {
            Long employeeId = jwtProvider.getEmployeeId(accessToken);
            String refreshToken = request.getHeader(REFRESH_HEADER_STRING);
            log.info("===================== REFRESH-TOKEN " + refreshToken);
            if (refreshToken == null) {
                log.info("===================== NOT REFRESH-TOKEN");
                throw new ApiException(ErrorStatus._JWT_REFRESH_TOKEN_NOT_FOUND);
            } else if (!jwtProvider.checkRefreshTokenInRedis(employeeId, refreshToken)) {
                log.info("===================== DIFF REFRESH-TOKEN");
                throw new ApiException(ErrorStatus._JWT_DIFF_REFRESH_TOKEN_IN_REDIS);
            }
            jwtProvider.getAuthentication(accessToken);
            log.info("===================== LOGIN SUCCESS");
            log.info("===================== EMPLOYEE ID : " + employeeId);
        } else {
            log.info("===================== INVALID ACCESS-TOKEN");
            request.setAttribute("exception", ErrorStatus._JWT_INVALID);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestUrl) {
        return requestUrl.equals("/api") ||
                requestUrl.equals("/api/login") ||
                requestUrl.startsWith("/swagger-ui/**") ||
                requestUrl.startsWith("/swagger-resources/**") ||
                requestUrl.startsWith("/v3/api-docs/**") ||
                requestUrl.startsWith("/favicon.ico");
    }

    private boolean isBearer(String authorizationHeader) {
        return authorizationHeader.startsWith(TOKEN_PREFIX);
    }

    private boolean checkBlackList(String accessToken) {
        return redisUtil.hasKeyBlackList(accessToken);
    }
}
