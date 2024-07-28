package clovider.clovider_be.domain.notice.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.QNotice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeResponse> searchNotices(SearchType searchType, String keyword) {
        QNotice notice = QNotice.notice;

        return queryFactory.selectFrom(notice)
                .where(buildPredicate(searchType, keyword))
                .fetch()
                .stream()
                .map(Notice::toNoticeResponse)
                .toList();
    }

    private BooleanExpression buildPredicate(SearchType searchType, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return switch (searchType) {
            case TITLE -> QNotice.notice.title.containsIgnoreCase(keyword);
            case CONTENT -> QNotice.notice.content.containsIgnoreCase(keyword);
            case BOTH -> QNotice.notice.title.containsIgnoreCase(keyword)
                    .or(QNotice.notice.content.containsIgnoreCase(keyword));
        };
    }

}
