package clovider.clovider_be.domain.qnaImage.dto;

import clovider.clovider_be.domain.qnaImage.QnaImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaImageResponse {

    private Long qnaImageId;
    private String image;

    public static QnaImageResponse toQnaImageResponse(
            QnaImage qnaImage) {
        return QnaImageResponse.builder()
                .qnaImageId(qnaImage.getId())
                .image(qnaImage.getImage())
                .build();
    }
}
