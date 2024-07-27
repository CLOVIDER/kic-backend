package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.lottery.QLottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LotteryRepositoryCustomImpl implements LotteryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CompetitionRate> findCompetitionRates(List<Recruit> recruits) {

        QLottery lottery = QLottery.lottery;

        return jpaQueryFactory.select(Projections.fields(CompetitionRate.class,
                        lottery.recruit.id.as("recruitId"),
                        Expressions.numberTemplate(Double.class,
                                "CAST(COUNT({0}) AS double) / {1}",
                                lottery.id,
                                lottery.recruit.recruitCnt
                        ).as("competitionRate")))
                .from(lottery)
                .where(lottery.recruit.in(recruits))
                .groupBy(lottery.recruit.id)
                .fetch();
    }
}
