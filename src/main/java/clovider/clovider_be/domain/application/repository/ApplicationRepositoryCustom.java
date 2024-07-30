package clovider.clovider_be.domain.application.repository;

import clovider.clovider_be.domain.admin.dto.AdminResponse.ApplicationList;
import clovider.clovider_be.domain.admin.dto.SearchVO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepositoryCustom {

    Page<ApplicationList> getApplicationPage(List<Long> applicationIds, Pageable pageable,
            SearchVO searchVO);
}
