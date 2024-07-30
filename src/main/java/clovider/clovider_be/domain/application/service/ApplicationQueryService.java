package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import clovider.clovider_be.domain.application.dto.ApplicationReadDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationQueryService {

    ApplicationReadDto applicationRead(Long Id);

    void applicationPagination();

    Page<ApplicationList> getNowApplications(List<Long> applicationIds, Pageable pageable,
            SearchVO searchVO);
}