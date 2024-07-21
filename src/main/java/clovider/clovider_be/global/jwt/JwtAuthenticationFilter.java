package clovider.clovider_be.global.jwt;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.global.util.JwtProperties;
import clovider.clovider_be.global.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final EmployeeQueryService employeeQueryService;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (isPublicUrl(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = extractAccessToken(request);
        String refreshToken = extractRefreshToken(request);

        log.info("===================== AccessToken : " + accessToken);

        if (!accessToken.isEmpty()) {
            if (redisUtil.hasKeyBlackList(accessToken)) {
                log.info("===================== BLACKLIST LOGIN");
                request.setAttribute("exception", ErrorStatus._JWT_BLACKLIST);
            } else if (jwtProvider.validateToken(accessToken)) {
                SecurityContextHolder.getContext()
                        .setAuthentication(jwtProvider.getAuthentication(accessToken));
                log.info("===================== LOGIN SUCCESS");
            } else if (!jwtProvider.validateToken(accessToken) && refreshToken != null) {
                Boolean isValidated = jwtProvider.validateToken(refreshToken);
                Long employeeId = jwtProvider.getEmployeeId(refreshToken);
                Boolean hasRefreshToken = jwtProvider.existsRefreshTokenInRedis(employeeId);

                if (isValidated && hasRefreshToken) {
                    Employee employee = employeeQueryService.getEmployee(employeeId);
                    String newAccessToken = jwtProvider.generateAccessToken(employee.getId(),
                            employee.getRole());

                    SecurityContextHolder.getContext()
                            .setAuthentication(jwtProvider.getAuthentication(newAccessToken));
                    log.info("===================== ReIssue AccessToken : " + newAccessToken);
                } else {
                    request.setAttribute("exception", ErrorStatus._JWT_INVALID);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestUrl) {
        return requestUrl.equals("/api") ||
                requestUrl.equals("/api/login") ||
                requestUrl.startsWith("/swagger-ui") ||
                requestUrl.startsWith("/v3/api-docs") ||
                requestUrl.startsWith("/favicon.ico");
    }

    private boolean isBearer(String authorizationHeader) {
        return authorizationHeader.startsWith(JwtProperties.TOKEN_PREFIX);
    }

    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        if (isBearer(bearerToken)) {
            return bearerToken.substring(7);
        } else {
            request.setAttribute("exception", ErrorStatus._JWT_NOT_FOUND);
        }
        return null;
    }

    public String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);

        if (refreshToken.startsWith(JwtProperties.REFRESH_HEADER_STRING)) {
            return refreshToken;
        }
        return null;
    }
}
