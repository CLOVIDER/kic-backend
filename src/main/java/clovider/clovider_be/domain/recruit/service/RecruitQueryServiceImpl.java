package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "nowRecruit", key = "'sorted'")
    public NowRecruits getNowRecruitOrderByClass() {

        List<NowRecruit> recruits = recruitRepository.findNowRecruitOrderByClass(
                LocalDateTime.now());

        return new NowRecruits(recruits);
    }

    @Override
    @Cacheable(value = "recruit", key = "'ing'")
    public List<Long> getRecruitIngAndScheduled() {
        return recruitRepository.findRecruitIngAndScheduled(LocalDateTime.now());
    }

    @Override
    public List<Recruit> getNowRecruit() {
        return recruitRepository.findNowRecruit(LocalDateTime.now());
    }

    @Override
    public RecruitInfo getRecruitInfo(Long recruitId) {

        Recruit recruit = recruitRepository.findRecruitInfoById(recruitId)
                .orElseThrow(() -> new ApiException(
                        ErrorStatus._RECRUIT_NOT_FOUND));

        return LotteryResponse.toRecruitInfo(recruit);
    }

    @Override
    public Recruit getRecruit(Long id) {
        return recruitRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND)
        );
    }

    @Override
    public List<Recruit> getRecruitAndKindergarten() {
        return recruitRepository.findRecruitKdg(LocalDateTime.now());
    }
}
