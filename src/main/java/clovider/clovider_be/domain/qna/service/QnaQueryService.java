package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface QnaQueryService {

    Qna findById(Long id);

    QnaResponse getQna(Long qnaId);

    CustomPage<QnaResponse> getAllQnas(int page, int size);

    Integer getWaitQna();

    List<QnaResponse> searchQnas(SearchType type, String keyword);
}
