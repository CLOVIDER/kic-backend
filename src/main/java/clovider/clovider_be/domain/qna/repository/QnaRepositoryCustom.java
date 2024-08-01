package clovider.clovider_be.domain.qna.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepositoryCustom {
    List<QnaResponse> searchQnas(SearchType type, String keyword);
}
