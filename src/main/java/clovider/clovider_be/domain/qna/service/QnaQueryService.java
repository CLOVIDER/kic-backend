package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.qna.Qna;
import org.springframework.stereotype.Service;

@Service
public interface QnaQueryService {

    Qna findById(Long id);
}
