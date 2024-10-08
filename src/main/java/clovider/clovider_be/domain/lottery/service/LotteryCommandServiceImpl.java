package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.WeightCalculationDTO;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
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


import static clovider.clovider_be.domain.enums.Result.*;
import static clovider.clovider_be.domain.enums.Result.WAIT;

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

    private final ApplicationQueryService applicationQueryService;
    private final RecruitQueryService recruitQueryService;

    @Override
    public LotteryResponseDTO createLottery(Long recruitId) {



            Recruit recruit = recruitRepository.findById(recruitId)
                    .orElseThrow(() -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND));

            if(recruit.getIsDrew() == '1'){
                return new LotteryResponseDTO(
                        true,
                        "COMMON200",
                        "이미 진행된 추첨입니다.",
                        new LotteryResponseDTO.Result(recruit.getId(), recruit.getCreatedAt())
                );
            }

            List<Application> applications = lotteryRepository.findAllApplicationByRecruitId(recruit.getId());

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
                        app.getIsDualIncome(),
                        recruit
                );

                double weight = weightDTO.calculateWeight();
                applicantData.put("weight", weight);
                applicants.add(applicantData);
            }

            int recruitCnt = recruit.getRecruitCnt();

            List<Map<String, Object>> selectedApplicants;

            if (applications.size() <= recruitCnt) {
                // 지원자 수가 모집 정원에 못 미칠 경우 모든 지원자를 합격 처리
                selectedApplicants = new ArrayList<>(applicants);
            } else {
                // 지원자 수가 모집 정원을 초과할 경우 추첨 로직 수행
                selectedApplicants = WeightedRandomSelection.weightedRandomSelection(applicants, recruitCnt);
            }

        // 순서대로 업데이트
        int rank = 1;

        // 전체 신청서에 대한 순번 부여
        for (Map<String, Object> applicant : applicants) {
            Long applicantId = (Long) applicant.get("id");
            boolean isSelected = selectedApplicants.stream()
                    .anyMatch(selectedApplicant -> selectedApplicant.get("id").equals(applicantId));

            Result result = isSelected ? Result.WIN : Result.LOSE;

            Lottery lottery = lotteryRepository.findLotteryByApplicationIdAndRecruitId(applicantId, recruitId);

            if (isSelected) {
                lottery.setRankNo(0);  // 당첨자의 대기 순번은 0으로 설정
            } else {
                // 대기 순번 설정 (당첨된 신청서가 처리된 이후의 순서)
                lottery.setRankNo(rank++);
            }

            lottery.setResult(result);
            lottery.setIsRegistry('0');


            lotteryRepository.save(lottery);
        }
            recruit.setIsDrew('1');

            return new LotteryResponseDTO(
                    true,
                    "COMMON200",
                    "추첨이 생성 및 저장되었습니다.",
                    new LotteryResponseDTO.Result(recruit.getId(), recruit.getCreatedAt())
            );


    }


    @Override
    public LotteryResisterResponseDTO updateRegistry(Long lotteryId) {

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
    public void deleteLotteryBylotteryId(Long lotteryId) {
        Lottery lottery = lotteryRepository.findById(lotteryId)
                .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));

        if(lottery.getResult()==WAIT){
            lotteryRepository.delete(lottery);
        }
        //모집이 이미 진행완료일때에는 취소 불가 메시지 반환
        else if (lottery.getResult()==WIN || lottery.getResult()==LOSE){
            throw new ApiException(ErrorStatus._RECRUIT_CANNOT_CANCEL);
        }
    }

    //추첨 테이블에 값 입력
    @Override
    public void insertLottery(List<Map<String, Object>> childrenRecruitList, Long applicationId) {
        Application application = applicationQueryService.getApplication(applicationId);


        for (Map<String, Object> childRecruitData : childrenRecruitList) {
            String childName = (String) childRecruitData.get("childNm");
            List<Integer> recruitIds = (List<Integer>) childRecruitData.get("recruitIds");

            for (Integer recruitId : recruitIds) {
                Recruit recruit = recruitQueryService.getRecruit(Long.valueOf(recruitId));

                lotteryRepository.save(
                        Lottery.builder()
                                .application(application)
                                .recruit(recruit)
                                .rankNo(0)
                                .result(WAIT)
                                .isRegistry('0')
                                .childNm(childName)
                                .build()
                );
            }
        }
    }

    //추첨 테이블 값 일괄 삭제
    @Override
    public void deleteLottery(Long applicationId) {
        Application application = applicationQueryService.getApplication(applicationId);
        List<Lottery> savedLotteries = lotteryRepository.findAllByApplication(application);
        lotteryRepository.deleteAll(savedLotteries);
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
            throw new ApiException(ErrorStatus._EXTERNAL_API_ERROR);
        }
    }

    public List<Map<String, Object>> createApplicantsList(Recruit recruit) {

        Long recruitId = recruit.getId();
        List<Application> applications = lotteryRepository.findAllApplicationByRecruitId(recruitId);

        List<Map<String, Object>> applicants = new ArrayList<>();
        for (Application app : applications) {
            Map<String, Object> applicantData = new HashMap<>();

            Long applicationId = app.getId();
            Long RestLotteryId = lotteryRepository.findLotteryIdByApplication(applicationId, recruitId);
            applicantData.put("id", RestLotteryId);

            WeightCalculationDTO weightDTO = new WeightCalculationDTO(
                app.getWorkYears(),
                app.getIsSingleParent(),
                app.getChildrenCnt(),
                app.getIsDisability(),
                app.getIsEmployeeCouple(),
                app.getIsSibling(),
                app.getIsDualIncome(),
                recruit
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
