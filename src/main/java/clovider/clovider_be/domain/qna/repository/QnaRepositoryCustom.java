package clovider.clovider_be.domain.qna.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepositoryCustom {
    Page<QnaResponse> searchQnas(Pageable pageable, SearchType type, String keyword);
}
