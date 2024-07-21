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

    // 직원(관리자) 관련
    _ADMIN_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "ADMIN001", "관리자를 찾을 수 없습니다."),

    // 직원 관련
    _EMPLOYEE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "EMPLOYEE001", "직원을 찾을 수 없습니다."),

    // 공사사항 관련
    _NOTICE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "NOTICE001", "공지사항을 찾을 수 없습니다."),

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