package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import org.springframework.stereotype.Service;

@Service
public interface QnaCommandService {

    CustomResult createQna(QnaRequest qnaRequest);

    CustomResult updateQna(Long qnaId, QnaRequest qnaRequest);
}
