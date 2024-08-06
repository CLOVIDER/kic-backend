package clovider.clovider_be.domain.notice.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepositoryCustom {
    Page<NoticeResponse> searchNotices(Pageable pageable, SearchType type, String keyword);
}
