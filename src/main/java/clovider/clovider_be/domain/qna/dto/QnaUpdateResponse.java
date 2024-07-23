package clovider.clovider_be.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QnaUpdateResponse {

    private Long qnaId;

    public static QnaUpdateResponse of(Long qnaId) {
        return QnaUpdateResponse.builder().qnaId(qnaId).build();
    }
}
