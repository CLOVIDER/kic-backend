package clovider.clovider_be.global.jwt;

import clovider.clovider_be.global.response.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
        log.info("===================== Exception Control : " + request.getAttribute("exception"));

        if (exception.equals(ErrorStatus._JWT_NOT_FOUND)) {
            exceptionHandler(response, ErrorStatus._JWT_NOT_FOUND);
        } else if (exception.equals(ErrorStatus._JWT_INVALID)) {
            exceptionHandler(response, ErrorStatus._JWT_INVALID);
        } else if (exception.equals(ErrorStatus._JWT_BLACKLIST)) {
            exceptionHandler(response, ErrorStatus._JWT_BLACKLIST);
        }

    }

    public void exceptionHandler(HttpServletResponse response, ErrorStatus errorStatus)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject responseJson = new JSONObject();
        responseJson.put("isSuccess", "False");
        responseJson.put("message", errorStatus.getMessage());
        responseJson.put("code", errorStatus.getCode());

        response.getWriter().print(responseJson);

    }
}
