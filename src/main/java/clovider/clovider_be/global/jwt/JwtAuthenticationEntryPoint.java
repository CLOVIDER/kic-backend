package clovider.clovider_be.global.jwt;

import static clovider.clovider_be.global.util.ExceptionHandlerUtil.exceptionHandler;

import clovider.clovider_be.global.response.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        ErrorStatus exception = (ErrorStatus) request.getAttribute("exception");
        log.info("===================== EntryPoint - Exception Control : " + request.getAttribute(
                "exception"));

        if (exception.equals(ErrorStatus._JWT_NOT_FOUND)) {
            exceptionHandler(response, ErrorStatus._JWT_NOT_FOUND,
                    HttpServletResponse.SC_UNAUTHORIZED);
        } else if (exception.equals(ErrorStatus._JWT_INVALID)) {
            exceptionHandler(response, ErrorStatus._JWT_INVALID, HttpServletResponse.SC_UNAUTHORIZED);
        } else if (exception.equals(ErrorStatus._JWT_EXPIRED)) {
            exceptionHandler(response, ErrorStatus._JWT_EXPIRED, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
