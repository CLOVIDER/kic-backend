package clovider.clovider_be.global.response.code.status;

import clovider.clovider_be.global.response.code.BaseErrorCode;
import clovider.clovider_be.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Employee 관련
    _EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "EMPLOYEE_001", "요청 직원 정보는 존재하지 않습니다."),

    // 직원(관리자) 관련
    _ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN001", "관리자를 찾을 수 없습니다."),

    // 직원 관련
//    _EMPLOYEE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "EMPLOYEE001", "직원을 찾을 수 없습니다."),

    _KDG_NOT_FOUND(HttpStatus.NOT_FOUND, "KDG001", "해당 어린이집을 찾을 수 없습니다."),

    //신청서 관련
    _APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICATION001", "해당 신청서를 찾을 수 없습니다."),


    // 공사사항 관련
    _NOTICE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "NOTICE001", "공지사항을 찾을 수 없습니다."),
  
    // JWT 관련
    _JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT_001", "Header에 JWT가 존재하지 않습니다."),
    _JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT_002", "검증되지 않는 JWT 입니다."),
    _JWT_BLACKLIST(HttpStatus.UNAUTHORIZED, "JWT_003", "블랙 리스트 토큰입니다. 다시 로그인 해주세요"),
    _JWT_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "JWT_004", "아이디와 비밀번호 정보를 다시 확인해주세요"),
    _JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT_005",
            "Header에 RefreshToken이 존재하지 않습니다."),
    _JWT_DIFF_REFRESH_TOKEN_IN_REDIS(HttpStatus.UNAUTHORIZED, "JWT_006",
            "Redis에 존재하는 Refresh Token과 다릅니다."),

    // S3 관련
    _S3_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_001", "S3에 존재하지 않는 이미지입니다."),

    // qna 관련
    _QNA_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "QNA001", "qna를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }
}