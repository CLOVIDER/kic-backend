package clovider.clovider_be.domain.notice.repository;

import static clovider.clovider_be.domain.notice.QNotice.notice;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.QNotice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NoticeResponse> searchNotices(Pageable pageable, SearchType type, String keyword) {

        List<NoticeResponse> content = queryFactory.selectFrom(notice)
                .where(buildPredicate(type, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(NoticeResponse::toNoticeResponse)
                .toList();

        JPAQuery<Long> count = queryFactory.select(notice.count())
                .from(notice)
                .where(buildPredicate(type, keyword));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
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
