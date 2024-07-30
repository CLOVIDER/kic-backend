package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.employee.Employee;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationQueryService {

    Page<ApplicationList> getNowApplications(List<Long> applicationIds, Pageable pageable,
            SearchVO searchVO);

    ApplicationResponse applicationRead(Employee employee);

    ApplicationResponse applicationIdRead(Long Id);

    CustomPage<ApplicationResponse> applicationListRead(int page, int size);
}
