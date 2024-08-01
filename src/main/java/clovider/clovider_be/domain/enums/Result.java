package clovider.clovider_be.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Result {
    WIN("당첨"),
    LOSE("낙첨"),
    WAIT("대기");

    private final String description;
}
