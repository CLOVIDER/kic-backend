package clovider.clovider_be.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeClass {

    INFANT("0~2세"),
    TODDLER("3~5세"),
    KID("6~7세");

    private final String description;
}
