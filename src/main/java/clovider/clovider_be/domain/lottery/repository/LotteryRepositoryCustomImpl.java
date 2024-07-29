package clovider.clovider_be.domain.lottery.repository;

import static clovider.clovider_be.domain.application.QApplication.application;
import static clovider.clovider_be.domain.employee.QEmployee.employee;
import static clovider.clovider_be.domain.lottery.QLottery.lottery;
import static clovider.clovider_be.domain.recruit.QRecruit.recruit;

import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.recruit.Recruit;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .where(lottery.recruit.in(recruits).and(lottery.isAccept.eq(Accept.WAIT)))
                .fetchFirst();
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

    @Override
    public Page<Lottery> findAllByRecruits(List<Recruit> recruits, Pageable pageable,
            SearchVO searchVO) {

        // 1단계: 페이징 조회 - all 신청서 아이디로 추첨 페이징 조회
        List<Lottery> content = jpaQueryFactory
                .select(lottery)
                .from(lottery)
                .join(lottery.application, application).fetchJoin()
                .join(lottery.application.employee, employee).fetchJoin()
                .where(lottery.recruit.in(recruits), searchEmployee(searchVO.value()),
                        filterAccept(searchVO.filter()))
                .orderBy(lottery.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2단계: Count 쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(lottery.count())
                .from(lottery);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchEmployee(String value) {

        return value != null ? employee.accountId.containsIgnoreCase(value) : null;
    }

    private BooleanExpression filterAccept(String filter) {

        return !filter.equals("ALL") ? lottery.isAccept.eq(Accept.valueOf(filter)) : null;
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
