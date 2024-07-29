package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitQueryServiceImpl implements RecruitQueryService {

    private final RecruitRepository recruitRepository;

    @Override
    public List<Recruit> getRecruitByKindergarten(Long kindergartenId) {
        return recruitRepository.findByKindergartenId(kindergartenId);
    }

    @Override
    public List<Recruit> getNowRecruitPeriod() {

        return recruitRepository.findNowRecruit(LocalDateTime.now());
    }
}
