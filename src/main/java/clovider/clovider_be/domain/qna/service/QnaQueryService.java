package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import org.springframework.stereotype.Service;

@Service
public interface QnaQueryService {

    Qna findById(Long id);
    QnaResponse getQna(Long qnaId);
}
