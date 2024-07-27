package clovider.clovider_be.domain.auth.dto;

public record TokenVo(String accessToken, String refreshToken, String role) {

}