package clovider.clovider_be.domain.recruit.repository;

import clovider.clovider_be.domain.recruit.Recruit;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepositoryCustom {

    List<Recruit> findNowRecruitOrderByClass(LocalDateTime now);

}
