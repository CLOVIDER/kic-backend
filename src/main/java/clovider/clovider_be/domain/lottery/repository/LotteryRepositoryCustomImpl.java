package clovider.clovider_be.domain.lottery.repository;

import static clovider.clovider_be.domain.lottery.QLottery.lottery;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.TotalApplication;
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
    public List<TotalApplication> findTotalApplication(List<Recruit> recruits) {

        List<Tuple> results = jpaQueryFactory
                .select(recruit.kindergarten.kindergartenNm,
                        recruit.ageClass,
                        lottery.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .where(lottery.recruit.in(recruits))
                .groupBy(recruit.kindergarten.id, recruit.ageClass)
                .fetch();

        return extractTotalApplication(results);
    }

    @Override
    public List<TotalApplication> findUnAcceptApplication(List<Recruit> recruits) {

        List<Tuple> results = jpaQueryFactory
                .select(recruit.kindergarten.kindergartenNm,
                        recruit.ageClass,
                        lottery.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .where(lottery.recruit.in(recruits).and(lottery.isAccept.eq(Accept.WAIT)))
                .groupBy(recruit.kindergarten.id, recruit.ageClass)
                .fetch();

        return extractTotalApplication(results);
    }

    @Override
    public List<AcceptResult> findAcceptStatus(List<Recruit> recruits) {

        List<Tuple> results = jpaQueryFactory
                .select(recruit.kindergarten.kindergartenNm,
                        lottery.isAccept,
                        lottery.isAccept.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .where(lottery.recruit.in(recruits))
                .groupBy(recruit.kindergarten.id, lottery.isAccept)
                .fetch();

        return extractAcceptResult(results);
    }

    private List<TotalApplication> extractTotalApplication(List<Tuple> results) {

        Map<String, TotalApplication> applicationMap = new HashMap<>();

        for (Tuple tuple : results) {
            String kdgNm = tuple.get(recruit.kindergarten.kindergartenNm);
            String ageClass = tuple.get(recruit.ageClass).toString();
            int count = tuple.get(lottery.count().as("cnt")).intValue();

            TotalApplication application = applicationMap.computeIfAbsent(kdgNm, nm ->
                    TotalApplication.builder()
                            .kindergartenNm(nm)
                            .totalCnt(0)
                            .infantCnt(0)
                            .toddlerCnt(0)
                            .kidCnt(0)
                            .build()
            );

            TotalApplication updatedApplication = TotalApplication.builder()
                    .kindergartenNm(application.getKindergartenNm())
                    .totalCnt(application.getTotalCnt() + count)
                    .infantCnt(ageClass.equals("INFANT") ? count : application.getInfantCnt())
                    .toddlerCnt(ageClass.equals("TODDLER") ? count : application.getToddlerCnt())
                    .kidCnt(ageClass.equals("KID") ? count : application.getKidCnt())
                    .build();
            applicationMap.put(kdgNm, updatedApplication);
        }

        return new ArrayList<>(applicationMap.values());
    }

    private List<AcceptResult> extractAcceptResult(List<Tuple> results) {

        Map<String, AcceptResult> acceptMap = new HashMap<>();

        for (Tuple tuple : results) {
            String kdgNm = tuple.get(recruit.kindergarten.kindergartenNm);
            String accept = tuple.get(lottery.isAccept).toString();
            int count = tuple.get(lottery.isAccept.count().as("cnt")).intValue();

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
