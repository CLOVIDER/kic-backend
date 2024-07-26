package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
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
    public LotteryResponseDTO createLottery(Long recruitId, Long applicationId) {
        log.info("Creating lottery");
        log.info("Attempting to find Recruit with ID: {}", recruitId);


        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruit ID"));
        log.info("Recruit: {}", recruit);


        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        log.info("Application: {}", application);

        // 모집 ID로 전체 신청자 목록 가져오기
        List<Application> applications = applicationRepository.findByRecruit(recruit);
        log.info("Recruit: {} applications: {}", recruitId, applications);

        // 신청서 ID와 weight를 포함하는 리스트 생성
        List<Map<String, Object>> applicants = new ArrayList<>();

        for (Application app : applications) {
            Map<String, Object> applicantData = new HashMap<>();
            applicantData.put("id", app.getId());
            log.info("Applicant data: {}", applicantData);

            // 신청서의 필드에서 weight 계산
            double weight = calculateWeight(
                    app.getWorkYears(),
                    app.getIsSingleParent(),
                    app.getChildrenCnt(),
                    app.getIsDisability(),
                    app.getIsEmployeeCouple(),
                    app.getIsSibling(),
                    app.getIsDualIncome()
            );
            log.info("Applicant weight: {}", weight);
            applicantData.put("weight", weight);
            applicants.add(applicantData);
            log.info("Applicant data: {}", applicantData);
        }

        // recruitCnt 가져오기
        int recruitCnt = recruit.getRecruitCnt();
        log.info("RecruitCnt: {}", recruitCnt);
        // 가중치 기반 추첨 수행
        List<Integer> selectedApplicants = WeightedRandomSelection.weightedRandomSelection(applicants, recruitCnt);
        log.info("Selected applicants: {}", selectedApplicants);

        // 선택된 지원자에 현재 신청자가 포함되어 있는지 확인
        boolean isSelected = selectedApplicants.contains(applicationId.intValue());

        String result = isSelected ? "당첨" : "낙첨";

        // 추첨 생성
        Lottery lottery = Lottery.builder()
                .recruit(recruit)
                .application(application)
                .rankNo(1)  // 랭크는 임시로 1로 설정 (필요시 수정)
                .result(Result.valueOf(result))
                .registry('1')  // 등록 여부는 임시로 true로 설정 (필요시 수정)
                .accept('1')  // 승인 여부는 임시로 true로 설정 (필요시 수정)
                .build();

        Lottery savedLottery = lotteryRepository.save(lottery);

        return new LotteryResponseDTO(
                true,
                "COMMON200",
                "추첨이 생성 및 저장되었습니다.",
                new LotteryResponseDTO.Result(savedLottery.getId(), savedLottery.getCreatedAt())
        );
    }

    private double calculateWeight(Integer workYears, Character singleParent, Integer childrenCnt, Character disability, Character employeeCouple, Character sibling, Character dualIncome) {
        double weight = 1.0;

        if (workYears != null && workYears > 0) weight += workYears * 1.0;
        if (singleParent != null && singleParent == '1') weight += 5.0;
        if (childrenCnt != null && childrenCnt >= 2) weight += 1.0;
        if (disability != null && disability == '1') weight += 4.0;
        if (dualIncome != null && dualIncome == '1') weight += 1.0;
        if (employeeCouple != null && employeeCouple == '1') weight += 5.0;
        if (sibling != null && sibling == '1') weight += 2.0;

        return weight;
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
