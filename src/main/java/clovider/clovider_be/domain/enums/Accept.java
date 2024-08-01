package clovider.clovider_be.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Accept {

    ACCEPT("승인"),
    UNACCEPT("미승인"),
    WAIT("승인대기");

    private final String description;


}
