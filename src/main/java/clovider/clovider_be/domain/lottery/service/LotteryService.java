package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
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
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruit ID"));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));


        // 이 아래 부분 erd 수정하고 구현 마저 해야 할듯.
        // 모집 ID로 전체 신청자 목록 가져오기
        List<Application> applications = lotteryRepository.findAllByRecruitId(recruitId);
        log.info("Recruit: {} applications: {}", recruitId, applications);

        // 신청서 ID와 weight를 포함하는 리스트 생성
        List<Map<String, Object>> applicants = new ArrayList<>();
        for (Application app : applications) {
            Map<String, Object> applicantData = new HashMap<>();
            applicantData.put("id", app.getId());

            // 신청서의 필드에서 weight 계산
            double weight = calculateWeight(
                    app.getWorkYears(),
                    app.getSingleParent(),
                    app.getChildrenCnt(),
                    app.getDisability(),
                    app.getEmployeeCouple(),
                    app.getSibling(),
                    app.getDualIncome()
            );
            applicantData.put("weight", weight);
            applicants.add(applicantData);
        }

        // recruitCnt 가져오기
        int recruitCnt = recruit.getRecruitCnt();

        // 가중치 기반 추첨 수행
        List<Integer> selectedApplicants = WeightedRandomSelection.weightedRandomSelection(applicants, recruitCnt);

        // 선택된 지원자에 현재 신청자가 포함되어 있는지 확인
        boolean isSelected = selectedApplicants.contains(applicationId.intValue());

        String result = isSelected ? "당첨" : "낙첨";

        //아래는 편의상 테스트용
        Integer rankNm = 1;
        Boolean registry = true;
        Boolean accept = true;

        // 추첨 생성
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

    private double calculateWeight(Integer workYears, Boolean singleParent, Integer childrenCnt, Boolean disability, Boolean employeeCouple, Boolean sibling, Boolean dualIncome) {
        double weight = 1.0;

        if (workYears != null && workYears > 0) weight += workYears * 1.0;
        if (singleParent != null && singleParent) weight += 5.0;
        if (childrenCnt != null && childrenCnt >= 2) weight += 1.0;
        if (disability != null && disability) weight += 4.0;
        if (dualIncome != null && dualIncome) weight += 1.0;
        if (employeeCouple != null && employeeCouple) weight += 5.0;
        if (sibling != null && sibling) weight += 2.0;

        return weight;
    }

    // 추첨 진행
    public static class WeightedRandomSelection {

        public static List<Integer> weightedRandomSelection(List<Map<String, Object>> applicants, int numSelected) {
            double totalWeight = applicants.stream()
                    .mapToDouble(applicant -> (double) applicant.get("weight"))
                    .sum();

            List<Double> cumulativeWeights = new ArrayList<>();
            double cumulativeSum = 0;
            for (Map<String, Object> applicant : applicants) {
                cumulativeSum += (double) applicant.get("weight");
                cumulativeWeights.add(cumulativeSum);
            }

            Set<Integer> selectedApplicantsSet = new HashSet<>();
            Random random = new Random();

            while (selectedApplicantsSet.size() < numSelected) {
                double value = random.nextDouble() * totalWeight;
                for (int i = 0; i < cumulativeWeights.size(); i++) {
                    if (value <= cumulativeWeights.get(i)) {
                        selectedApplicantsSet.add((int) applicants.get(i).get("id"));
                        break;
                    }
                }
            }

            return new ArrayList<>(selectedApplicantsSet);
        }
    }
}
