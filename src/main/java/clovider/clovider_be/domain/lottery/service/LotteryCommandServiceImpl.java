package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.WeightCalculationDTO;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LotteryCommandServiceImpl implements LotteryCommandService {

    private final LotteryRepository lotteryRepository;
    private final RecruitRepository recruitRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public LotteryResponseDTO createLottery(Long recruitId, Long applicationId) {
        log.info("Creating lottery");
        log.info("Attempting to find Recruit with ID: {}", recruitId);

        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND));
        log.info("Recruit: {}", recruit);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApiException(ErrorStatus._APPLICATION_NOT_FOUND));
        log.info("Application: {}", application);

        List<Application> applications = applicationRepository.findAllByRecruitId(recruit.getId());
        log.info("Recruit: {} applications: {}", recruitId, applications);

        List<Map<String, Object>> applicants = new ArrayList<>();

        for (Application app : applications) {
            Map<String, Object> applicantData = new HashMap<>();
            applicantData.put("id", app.getId());

            WeightCalculationDTO weightDTO = new WeightCalculationDTO(
                    app.getWorkYears(),
                    app.getIsSingleParent(),
                    app.getChildrenCnt(),
                    app.getIsDisability(),
                    app.getIsEmployeeCouple(),
                    app.getIsSibling(),
                    app.getIsDualIncome()
            );

            double weight = weightDTO.calculateWeight();
            applicantData.put("weight", weight);
            applicants.add(applicantData);
            log.info("Applicant data: {}", applicantData);
        }

        int recruitCnt = recruit.getRecruitCnt();
        log.info("RecruitCnt: {}", recruitCnt);

        List<Integer> selectedApplicants = WeightedRandomSelection.weightedRandomSelection(applicants, recruitCnt);
        log.info("Selected applicants: {}", selectedApplicants);

        boolean isSelected = selectedApplicants.contains(applicationId.intValue());

        String result = isSelected ? "당첨" : "낙첨";

        Lottery lottery = Lottery.builder()
                .recruit(recruit)
                .application(application)
                .rankNo(1)
                .result(Result.valueOf(result))
                .registry('1')
                .accept('1')
                .build();

        Lottery savedLottery = lotteryRepository.save(lottery);

        return new LotteryResponseDTO(
                true,
                "COMMON200",
                "추첨이 생성 및 저장되었습니다.",
                new LotteryResponseDTO.Result(savedLottery.getId(), savedLottery.getCreatedAt())
        );
    }

    @Override
    public LotteryResisterResponseDTO updateRegistry(Long lotteryId) {
        log.info("Updating registry for Lottery with ID: {}", lotteryId);

        Lottery lottery = lotteryRepository.findById(lotteryId)
                .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));
        Character registryStatus = lottery.getRegistry();


        Lottery updatedLottery = lotteryRepository.save(lottery);


        //등록
        if(registryStatus == '0') {
            lottery.setRegistry('1');
            return new LotteryResisterResponseDTO(
                    "등록되었습니다.",
                    new LotteryResisterResponseDTO.Result(updatedLottery.getId(), updatedLottery.getRegistry() == '1')
            );
        }

        //등록취소
        else {

            lottery.setRegistry('0');
            return new LotteryResisterResponseDTO(
                    "등록이 취소되었습니다.",
                    new LotteryResisterResponseDTO.Result(updatedLottery.getId(), updatedLottery.getRegistry() == '1')
            );
        }

    }


    // 추첨 진행
    public static class WeightedRandomSelection {

        public static List<Integer> weightedRandomSelection(List<Map<String, Object>> applicants, int numSelected) {
            double totalWeight = applicants.stream()
                    .mapToDouble(applicant -> (double) applicant.get("weight"))
                    .sum();
            log.info("Total weight: {}", totalWeight);

            List<Double> cumulativeWeights = new ArrayList<>();

            double cumulativeSum = 0;
            for (Map<String, Object> applicant : applicants) {
                cumulativeSum += (double) applicant.get("weight");
                cumulativeWeights.add(cumulativeSum);
            }
            log.info("Cumulative weights: {}", cumulativeWeights);

            Set<Integer> selectedApplicantsSet = new HashSet<>();
            log.info("Selected applicants: {}", selectedApplicantsSet);
            Random random = new Random();

            while (selectedApplicantsSet.size() < numSelected) {
                double value = random.nextDouble() * totalWeight;
                log.info("Selected applicant: {}", value);
                for (int i = 0; i < cumulativeWeights.size(); i++) {
                    if (value <= cumulativeWeights.get(i)) {
                        selectedApplicantsSet.add((int) applicants.get(i).get("id"));
                        log.info("Applicant selected: {}", applicants.get(i).get("id"));
                        break;
                    }
                }
            }
            log.info("Selected applicants: {}", selectedApplicantsSet);

            return new ArrayList<>(selectedApplicantsSet);
        }
    }
}
