package clovider.clovider_be.global.jwt;

import static clovider.clovider_be.global.util.JwtProperties.EMPLOYEE_ID_KEY;
import static clovider.clovider_be.global.util.JwtProperties.ROLE;

import clovider.clovider_be.domain.employee.Role;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.global.security.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final Long ACCESS_TOKEN_EXPIRE_TIME;
    private final Long REFRESH_TOKEN_EXPIRE_TIME;
    private final CustomUserDetailService customUserDetailService;
    private final Key key;

    public JwtProvider(@Value("${jwt.secret_key}") String secretKey,
            @Value("${jwt.access_token_expire}") Long accessTokenExpire,
            @Value("${jwt.refresh_token_expire}") Long refreshTokenExpire,
            CustomUserDetailService customUserDetailService) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpire;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpire;
        this.customUserDetailService = customUserDetailService;
    }

    /**
     * JWT header "alg" : "HS512" payload "id" : "employeeId" payload "auth" : "EMPLOYEE/ADMIN"
     * payload "iat" : "123456789" payload "exp" : "123456789"
     */
    public String generateAccessToken(Long employeeId, Role role) {

        Date expiredAt = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim(EMPLOYEE_ID_KEY, employeeId)
                .claim(ROLE, role)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {

        Date expiredAt = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getEmployeeId(String token) {
        return parseClaims(token)
                .get(EMPLOYEE_ID_KEY, Long.class);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorStatus._JWT_INVALID);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 정보가 잘못되었습니다.");
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        if (claims.get(EMPLOYEE_ID_KEY, Long.class) == null) {
            throw new JwtException("직원 정보가 없는 JWT 입니다.");
        }

        UserDetails userDetails = customUserDetailService.loadUserByUsername(
                claims.get(EMPLOYEE_ID_KEY, Long.class).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

}
