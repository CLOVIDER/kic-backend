package clovider.clovider_be.domain.lottery.repository;

import static clovider.clovider_be.domain.application.QApplication.application;
import static clovider.clovider_be.domain.employee.QEmployee.employee;
import static clovider.clovider_be.domain.lottery.QLottery.lottery;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LotteryRepositoryCustomImpl implements LotteryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CompetitionRate> findCompetitionRates(List<Long> recruitIds) {

        return jpaQueryFactory.select(Projections.fields(CompetitionRate.class,
                        lottery.recruit.id.as("recruitId"),
                        Expressions.numberTemplate(Double.class,
                                "CAST(COUNT({0}) AS double) / {1}",
                                lottery.id,
                                lottery.recruit.recruitCnt
                        ).as("competitionRate")))
                .from(lottery)
                .where(lottery.recruit.id.in(recruitIds))
                .groupBy(lottery.recruit.id)
                .fetch();
    }

    @Override
    public Long findTotalApplication(List<Long> recruitIds) {

        return jpaQueryFactory
                .select(lottery.count().as("cnt"))
                .from(lottery)
                .where(lottery.recruit.id.in(recruitIds))
                .fetchFirst();
    }

    @Override
    public Long findUnAcceptApplication(List<Long> recruitIds) {

        return jpaQueryFactory
                .select(lottery.count().as("cnt"))
                .from(lottery)
                .join(lottery.application, application)
                .where(lottery.recruit.id.in(recruitIds).and(application.isAccept.eq(Accept.WAIT)))
                .fetchFirst();
    }

    @Override
    public List<AcceptResult> findAcceptStatus(List<Long> recruitIds) {

        List<Tuple> results = jpaQueryFactory
                .select(recruit.kindergarten.kindergartenNm,
                        application.isAccept,
                        application.isAccept.count().as("cnt"))
                .from(lottery)
                .join(lottery.recruit, recruit)
                .join(lottery.application, application)
                .where(lottery.recruit.id.in(recruitIds))
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

    @Override
    public Page<LotteryResult> getLotteryResults(Long kindergartenId, Pageable pageable,
            String value) {

        List<LotteryResult> content = jpaQueryFactory
                .select(lottery)
                .from(lottery)
                .join(lottery.application, application).fetchJoin()
                .join(application.employee, employee).fetchJoin()
                // 조건절에서는 바로 필터링을 걸어서 굳이 엔티티 fetch join이 필요하지 않음
                // 만약 결과에 recruit와 kindergarten의 정보가 필요하고 이 정보를 바탕으로 데이터 조작을 해야 한다면 필요
                .where(lottery.recruit.kindergarten.id.in(kindergartenId),
                        searchEmployee(value),
                        lottery.recruit.recruitEndDt.lt(LocalDateTime.now()))
                .orderBy(lottery.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(AdminResponse::toLotteryResult)
                .toList();

        // 카운트 쿼리: 전체 결과 수를 계산하는 쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(lottery.count())
                .from(lottery)
                .join(lottery.application, application)
                .join(application.employee, employee)
                .where(lottery.recruit.kindergarten.id.eq(kindergartenId),
                        searchEmployee(value),
                        lottery.recruit.recruitEndDt.lt(LocalDateTime.now()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchEmployee(String value) {
        return value != null ? employee.accountId.containsIgnoreCase(value) : null;
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
