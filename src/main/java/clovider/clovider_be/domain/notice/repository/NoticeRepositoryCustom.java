package clovider.clovider_be.domain.notice.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepositoryCustom {
    List<NoticeResponse> searchNotices(SearchType type, String keyword);
}
