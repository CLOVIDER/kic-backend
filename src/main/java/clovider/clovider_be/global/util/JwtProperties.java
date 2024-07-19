package clovider.clovider_be.global.util;

public interface JwtProperties {

    String TOKEN_PREFIX = "Bearer ";
    String ACCESS_HEADER_STRING = "Authorization";
    String REFRESH_HEADER_STRING = "Refresh-Token";
    String ROLE = "role";
    String EMPLOYEE_ID_KEY = "id";
}