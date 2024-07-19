package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LotteryService {

    private final LotteryRepository lotteryRepository;
    private final RecruitRepository recruitRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public LotteryService(LotteryRepository lotteryRepository, RecruitRepository recruitRepository, ApplicationRepository applicationRepository) {
        this.lotteryRepository = lotteryRepository;
        this.recruitRepository = recruitRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public LotteryResponseDTO createLottery(Long recruitId, Long applicationId, Integer rankNm, String result, Boolean registry, Boolean accept) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruit ID"));
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));

        Lottery lottery = Lottery.builder()
                .recruit(recruit)
                .application(application)
                .rankNm(rankNm)
                .result(result)
                .registry(registry)
                .accept(accept)
                .build();

        Lottery savedLottery = lotteryRepository.save(lottery);

        return new LotteryResponseDTO(
                true,
                "COMMON200",
                "추첨이 생성 및 저장되었습니다.",
                new LotteryResponseDTO.Result(savedLottery.getId(), savedLottery.getCreatedAt())
        );
    }
}
