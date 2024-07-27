package clovider.clovider_be.global.auth.dto;

public record TokenVo(String accessToken, String refreshToken, String role) {

}