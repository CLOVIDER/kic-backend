package clovider.clovider_be.domain.recruit.repository;

import static clovider.clovider_be.domain.kindergarten.QKindergarten.kindergarten;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<NowRecruit> findNowRecruitOrderByClass(LocalDateTime now) {

        return jpaQueryFactory
                .selectFrom(recruit)
                .join(recruit.kindergarten, kindergarten).fetchJoin()
                .where(recruiting(now).or(scheduledRecruit(now)))
                .orderBy(recruit.kindergarten.id.asc(), recruit.ageClass.asc())
                .fetch()
                .stream().map(RecruitResponse::toNowRecruit).toList();
    }

    @Override
    public List<Long> findRecruitIngAndScheduled(LocalDateTime now) {

        return jpaQueryFactory
                .select(recruit.id)
                .from(recruit)
                .where(recruiting(now).or(scheduledRecruit(now)))
                .fetch();
    }

    private BooleanExpression recruiting(LocalDateTime now) {

        return recruit.recruitStartDt.loe(now).and(recruit.secondEndDt.goe(now));
    }

    private BooleanExpression scheduledRecruit(LocalDateTime now) {

        return recruit.recruitStartDt.gt(now);
    }

}
