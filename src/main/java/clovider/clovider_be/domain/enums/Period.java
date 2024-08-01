package clovider.clovider_be.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Period {

    SCHEDULED("모집예정"),
    ING("모집기간"),
    FIRST_REGISTRY("1차등록기간"),
    SECOND_REGISTRY("2차등록기간"),
    NOT_RECRUIT("모집없음");

    private final String description;
}
