package clovider.clovider_be.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.swing.JLabel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentType {

    RESIDENT_REGISTER("주민등록등본"),
    DUAL_INCOME("맞벌이 여부 서류"),
    SINGLE_PARENT("한부모 가정 서류"),
    DISABILITY("장애 증빙 서류"),
    MULTI_CHILDREN("다자녀 서류"),
    SIBLING("형제/자매 재원 서류");

    private final String description;
}
