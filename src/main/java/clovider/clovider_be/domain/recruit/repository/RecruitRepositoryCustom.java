package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepositoryCustom {

    List<NowRecruit> findNowRecruitOrderByClass(LocalDateTime now);

    List<Long> findRecruitIngAndScheduled(LocalDateTime now);

}
