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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import org.springframework.web.client.RestTemplate;

import static clovider.clovider_be.domain.enums.Accept.UNACCEPT;
import static clovider.clovider_be.domain.enums.Result.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LotteryCommandServiceImpl implements LotteryCommandService {

    private final LotteryRepository lotteryRepository;
    private final RecruitRepository recruitRepository;
    private final ApplicationRepository applicationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cloud.aws.lambda.url}")
    private String apiUrl;

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

        List<Application> applications = lotteryRepository.findApplicationByRecruitId(recruit.getId());
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

            double weight =  weightDTO.calculateWeight();
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
                .isRegistry('1')
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
        Character registryStatus = lottery.getIsRegistry();


        Lottery updatedLottery = lotteryRepository.save(lottery);


        //등록
        if(registryStatus == '0') {
            lottery.setIsRegistry('1');
            return new LotteryResisterResponseDTO(
                    "등록되었습니다.",
                    new LotteryResisterResponseDTO.Result(updatedLottery.getId(), updatedLottery.getIsRegistry() == '1')
            );
        }

        //등록취소
        else {

            lottery.setIsRegistry('0');
            return new LotteryResisterResponseDTO(
                    "등록이 취소되었습니다.",
                    new LotteryResisterResponseDTO.Result(updatedLottery.getId(), updatedLottery.getIsRegistry() == '1')
            );
        }

    }

    @Override
    public void deleteLottery(Long lotteryId) {
        Lottery lottery = lotteryRepository.findById(lotteryId)
                .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));

        if(lottery.getResult()==WAIT){
            lotteryRepository.delete(lottery);
        }
        else if (lottery.getResult()==WIN || lottery.getResult()==LOSE){
            throw new ApiException(ErrorStatus._RECRUIT_CANNOT_CANCEL);
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

    // 추첨 확률 받기
    public Double getPercent(Long lotteryId) {
        Lottery lottery = lotteryRepository.findById(lotteryId)
            .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));

        Recruit recruit = lottery.getRecruit();
        int recruitCnt = recruit.getRecruitCnt();

        List<Map<String, Object>> applicants = createApplicantsList(recruit);
        Map<String, Object> requestBody = createRequestBody(applicants, lotteryId, recruitCnt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);


        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);
            if (response.getBody() != null) {
                String body = (String) response.getBody().get("body");
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseBody = objectMapper.readValue(body, Map.class);

                if (responseBody != null && responseBody.containsKey("probability")) {
                    return (Double) responseBody.get("probability");
                } else {
                    throw new ApiException(ErrorStatus._EXTERNAL_API_ERROR);
                }
            } else {
                throw new ApiException(ErrorStatus._EXTERNAL_API_ERROR);
            }
        } catch (Exception e) {
            log.error("Error occurred while getting percentage: ", e);
            throw new ApiException(ErrorStatus._EXTERNAL_API_ERROR);
        }
    }

    public List<Map<String, Object>> createApplicantsList(Recruit recruit) {

        List<Application> applications = lotteryRepository.findAllApplicationByRecruitId(recruit.getId());

        List<Map<String, Object>> applicants = new ArrayList<>();
        for (Application app : applications) {
            Map<String, Object> applicantData = new HashMap<>();

            Long applicationId = app.getId();
            Long RestLotteryId = lotteryRepository.findLotteryIdByApplication(applicationId);
            applicantData.put("id", RestLotteryId);

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
        }

        return applicants;
    }

    public Map<String, Object> createRequestBody(List<Map<String, Object>> applicants, Long lotteryId, int recruitCnt) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("applicants", applicants);
        requestBody.put("target_id", lotteryId);
        requestBody.put("num_selected", recruitCnt);

        return requestBody;
    }

}
