package clovider.clovider_be.domain.qna.repository;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.QQna;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QnaRepositoryCustomImpl implements QnaRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<QnaResponse> searchQnas(SearchType type, String keyword) {
        QQna qna = QQna.qna;

        return queryFactory.selectFrom(qna)
                .where(buildPredicate(type,keyword))
                .fetch()
                .stream()
                .map(QnaResponse::toQnaResponse)
                .toList();

    }

    private BooleanExpression buildPredicate(SearchType searchType, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return switch (searchType) {
            case TITLE -> QQna.qna.title.containsIgnoreCase(keyword);
            case CONTENT -> QQna.qna.question.containsIgnoreCase(keyword);
            case BOTH -> QQna.qna.title.containsIgnoreCase(keyword)
                    .or(QQna.qna.question.containsIgnoreCase(keyword));
        };
    }

}
