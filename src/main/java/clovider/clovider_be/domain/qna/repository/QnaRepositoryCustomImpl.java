package clovider.clovider_be.domain.qna.repository;

import static clovider.clovider_be.domain.qna.QQna.qna;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
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
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<QnaResponse> searchQnas(Pageable pageable, SearchType type, String keyword) {

        // Fetch results with pagination
        List<QnaResponse> content = queryFactory.selectFrom(qna)
                .where(buildPredicate(type, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(QnaResponse::toQnaResponse)
                .toList();

        // Count total results
        JPAQuery<Long> countQuery = queryFactory.select(qna.count())
                .from(qna)
                .where(buildPredicate(type, keyword));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression buildPredicate(SearchType searchType, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return switch (searchType) {
            case TITLE -> qna.title.containsIgnoreCase(keyword);
            case CONTENT -> qna.question.containsIgnoreCase(keyword);
            case BOTH -> qna.title.containsIgnoreCase(keyword)
                    .or(qna.question.containsIgnoreCase(keyword));
        };
    }
}