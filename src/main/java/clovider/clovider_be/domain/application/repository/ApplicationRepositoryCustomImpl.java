package clovider.clovider_be.domain.application.repository;

import static clovider.clovider_be.domain.application.QApplication.application;
import static clovider.clovider_be.domain.employee.QEmployee.employee;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.enums.Accept;
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
public class ApplicationRepositoryCustomImpl implements ApplicationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ApplicationList> getApplicationPage(List<Long> applicationIds, Pageable pageable,
            SearchVO searchVO) {

        List<ApplicationList> content = jpaQueryFactory
                .select(application)
                .from(application)
                .join(application.employee, employee).fetchJoin()
                .where(application.id.in(applicationIds), searchEmployee(searchVO.value()),
                        filterAccept(searchVO.filter()))
                .orderBy(application.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(AdminResponse::toApplicationList)
                .toList();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(application.count())
                .from(application)
                .where(application.id.in(applicationIds), searchEmployee(searchVO.value()),
                        filterAccept(searchVO.filter()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchEmployee(String value) {

        return value != null ? employee.nameKo.containsIgnoreCase(value) : null;
    }

    private BooleanExpression filterAccept(String filter) {

        return !filter.equals("ALL") ? application.isAccept.eq(Accept.valueOf(filter)) : null;
    }
}
