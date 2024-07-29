package clovider.clovider_be.domain.recruit.repository;

import static clovider.clovider_be.domain.kindergarten.QKindergarten.kindergarten;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.recruit.Recruit;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RecruitRepositoryCustomImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Recruit> findNowRecruit(LocalDateTime now) {

        return jpaQueryFactory
                .selectFrom(recruit)
                .join(recruit.kindergarten, kindergarten)
                .fetchJoin()
                .where(recruit.recruitStartDt.loe(now).and(recruit.recruitEndDt.goe(now)))
                .orderBy(recruit.kindergarten.id.asc(), ageOrder().asc())
                .fetch();
    }

    private NumberExpression<Integer> ageOrder() {
        return new CaseBuilder()
                .when(recruit.ageClass.eq(AgeClass.INFANT)).then(1)
                .when(recruit.ageClass.eq(AgeClass.TODDLER)).then(2)
                .when(recruit.ageClass.eq(AgeClass.KID)).then(3)
                .otherwise(4);
    }
}
