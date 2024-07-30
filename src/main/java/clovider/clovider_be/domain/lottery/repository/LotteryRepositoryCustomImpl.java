package clovider.clovider_be.domain.lottery.repository;

import static clovider.clovider_be.domain.application.QApplication.application;
import static clovider.clovider_be.domain.lottery.QLottery.lottery;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LotteryRepositoryCustomImpl implements LotteryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CompetitionRate> findCompetitionRates(List<Recruit> recruits) {

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

    @Override
    public Long findTotalApplication(List<Recruit> recruits) {

        return jpaQueryFactory
                .select(lottery.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .where(lottery.recruit.in(recruits))
                .fetchFirst();
    }

    @Override
    public Long findUnAcceptApplication(List<Recruit> recruits) {

        return jpaQueryFactory
                .select(lottery.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .join(lottery.application, application)
                .where(lottery.recruit.in(recruits).and(application.isAccept.eq(Accept.WAIT)))
                .fetchFirst();
    }

    @Override
    public List<AcceptResult> findAcceptStatus(List<Recruit> recruits) {

        List<Tuple> results = jpaQueryFactory
                .select(recruit.kindergarten.kindergartenNm,
                        application.isAccept,
                        application.isAccept.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .join(lottery.application, application)
                .where(lottery.recruit.in(recruits))
                .groupBy(recruit.kindergarten.id, application.isAccept)
                .fetch();

        return extractAcceptResult(results);
    }

    @Override
    public List<Long> findApplicationsAllByRecruits(List<Recruit> recruits) {

        return jpaQueryFactory
                .selectDistinct(lottery.application.id)
                .from(lottery)
                .where(lottery.recruit.in(recruits))
                .fetch();
    }

    private List<AcceptResult> extractAcceptResult(List<Tuple> results) {

        Map<String, AcceptResult> acceptMap = new HashMap<>();

        for (Tuple tuple : results) {
            String kdgNm = tuple.get(recruit.kindergarten.kindergartenNm);
            String accept = tuple.get(application.isAccept).toString();
            int count = tuple.get(application.isAccept.count().as("cnt")).intValue();

            AcceptResult acceptResult = acceptMap.computeIfAbsent(kdgNm, nm ->
                    AcceptResult.builder()
                            .kindergartenNm(nm)
                            .acceptCnt(0)
                            .unAcceptCnt(0)
                            .waitCnt(0)
                            .build()
            );

            AcceptResult updatedAccept = AcceptResult.builder()
                    .kindergartenNm(acceptResult.getKindergartenNm())
                    .acceptCnt(accept.equals("ACCEPT") ? count : acceptResult.getAcceptCnt())
                    .unAcceptCnt(accept.equals("UNACCEPT") ? count : acceptResult.getUnAcceptCnt())
                    .waitCnt(accept.equals("WAIT") ? count : acceptResult.getWaitCnt())
                    .build();
            acceptMap.put(kdgNm, updatedAccept);
        }

        return new ArrayList<>(acceptMap.values());
    }

}
