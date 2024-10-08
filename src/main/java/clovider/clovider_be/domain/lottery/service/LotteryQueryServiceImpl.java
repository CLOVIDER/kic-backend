package clovider.clovider_be.domain.lottery.service;

import static clovider.clovider_be.domain.lottery.service.ConvertStringToList.convertStringToList;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryIdAndChildNameDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.ChildInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResultByEmployeeDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultsGroupedByChildDTO;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LotteryQueryServiceImpl implements LotteryQueryService {

    private final LotteryRepository lotteryRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationQueryService applicationQueryService;

    private final KindergartenClassRepository kindergartenClassRepository;
    private final RecruitRepository recruitRepository;

    @Override
    public LotteryResultResponseDTO getLotteryResultByLotteryId(Long lotteryId) {
        Lottery lottery = lotteryRepository.findById(lotteryId)
                .orElseThrow(() -> new ApiException(ErrorStatus._LOTTERY_NOT_FOUND));

        String KindergartenNm = applicationRepository.findKindergartenNameByLotteryId(lotteryId);

        LocalDateTime endDate = lottery.getRecruit().getFirstEndDt();

        return new LotteryResultResponseDTO(

                new LotteryResultResponseDTO.Result(lottery.getId(), lottery.getCreatedAt(),
                        lottery.getResult(), KindergartenNm, lottery.getRankNo(), endDate)
        );
    }


    @Override
    public List<CompetitionRate> getRecruitRates(List<NowRecruit> recruits) {

        List<Long> recruitIds = recruits.stream().map(NowRecruit::getId).toList();

        return lotteryRepository.findCompetitionRates(recruitIds);
    }

    @Override
    public Long getTotalApplication(List<Long> recruitIds) {
        return lotteryRepository.findTotalApplication(recruitIds);
    }

    @Override
    public Long getUnAcceptApplication(List<Long> recruitIds) {
        return lotteryRepository.findUnAcceptApplication(recruitIds);
    }

    @Override
    public List<AcceptResult> getAcceptResult(List<Long> recruitIds) {
        return lotteryRepository.findAcceptStatus(recruitIds);
    }

    @Override
    public List<RecruitResult> getRecruitResult(Long recruitId) {

        return LotteryResponse.toRecruitResults(lotteryRepository.findAllByRecruitId(recruitId));
    }

    @Override
    public List<RecruitResult> getRecruitIdsResult(List<Long> recruitIds) {

        return LotteryResponse.toRecruitResults(lotteryRepository.findAllByRecruitIds(recruitIds));
    }

    @Override
    public List<Long> getApplicationsByLotteries(List<Recruit> recruits) {

        return lotteryRepository.findApplicationsAllByRecruits(recruits);
    }

    @Override
    public List<ChildInfo> getChildInfos(Long applicationId) {
        List<Lottery> lotteries = lotteryRepository.findByApplicationId(applicationId);

        Map<String, List<RecruitInfo>> childInfoMap = new HashMap<>();
        for (Lottery lottery : lotteries) {
            String childName = lottery.getChildNm();
            Recruit recruit = lottery.getRecruit();
            RecruitInfo recruitInfo = LotteryResponse.toRecruitInfo(recruit);

            childInfoMap.computeIfAbsent(childName, k -> new ArrayList<>()).add(recruitInfo);
        }

        List<ChildInfo> childInfos = new ArrayList<>();
        for (Map.Entry<String, List<RecruitInfo>> entry : childInfoMap.entrySet()) {
            ChildInfo childInfo = ChildInfo.builder()
                    .childName(entry.getKey())
                    .recruitInfos(entry.getValue())
                    .build();
            childInfos.add(childInfo);
        }

        return childInfos;
    }

    @Override
    public Page<LotteryResult> getLotteryResultByLotteryId(Integer ageClass, Long kindergartenId,
            Pageable pageable, String value) {
        return lotteryRepository.getLotteryResults(ageClass, kindergartenId, pageable, value);
    }

    @Override
    public List<LotteryResultsGroupedByChildDTO> getLotteryResultsByEmployee(Employee employee) {

        List<Application> applications = applicationQueryService.getApplicationsByEmployee(
                employee);

        LocalDateTime currentTime = LocalDateTime.now();

        Map<String, List<LotteryResultByEmployeeDTO>> groupedResults = applications.stream()
                .flatMap(application -> lotteryRepository.findByApplicationId(application.getId())
                        .stream())
                .filter(lottery -> currentTime.isBefore(lottery.getRecruit().getRecruitEndDt()))
                .map(lottery -> LotteryResultByEmployeeDTO.builder()
                        .applicationId(lottery.getApplication().getId())
                        .lotteryId(lottery.getId())
                        .childName(lottery.getChildNm())
                        .kindergartenName(
                                lottery.getRecruit().getKindergarten().getKindergartenNm())
                        .ageClass(lottery.getRecruit().getAgeClass())
                        .result(lottery.getResult().name())
                        .isregistry(lottery.getIsRegistry())
                        .waitingNumber(
                                lottery.getResult() == Result.LOSE ? lottery.getRankNo() : null)
                        .build())
                .collect(Collectors.groupingBy(LotteryResultByEmployeeDTO::getChildName));

        return groupedResults.entrySet().stream()
                .map(entry -> LotteryResultsGroupedByChildDTO.builder()
                        .childName(entry.getKey())
                        .lotteryResults(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<LotteryIdAndChildNameDTO> getLotteryGroupedByChildNameByEmployeeId(Employee employee) {

        List<Object[]> results = lotteryRepository.findLotteryGroupedByChildNameByEmployee(employee);

        List<LotteryIdAndChildNameDTO> dtoList = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        for (Object[] result : results) {
            String childName = (String) result[0];
            String lotteryIdsStr = (String) result[1];
            List<Long> lotteryIds = convertStringToList(lotteryIdsStr);

            for (Long lotteryId : lotteryIds) {
                Lottery lottery = lotteryRepository.findById(lotteryId).orElse(null);

                if (lottery != null) {
                    Recruit recruit = lottery.getRecruit();
                    LocalDateTime recruitEndDt = recruit.getRecruitEndDt();

                    if (currentTime.isBefore(recruitEndDt)) {
                        Long recruitId = recruit.getId();
                        Long kindergartenId = recruitRepository.findKindergartenIdByRecruitId(recruitId);
                        int ageClass = recruitRepository.finAgeClassById(recruitId);
                        String className = kindergartenClassRepository.findClassNameById(kindergartenId, ageClass);

                        Long applicationId = lottery.getApplication().getId();
                        Optional<Application> application = applicationRepository.findById(applicationId);
                        Accept isAccept = application.map(Application::getIsAccept).orElse(null);

                        dtoList.add(LotteryIdAndChildNameDTO.builder()
                                .childName(childName)
                                .lotteryId(lotteryId)
                                .className(className)
                                .isAccept(isAccept)
                                .build());
                    }
                }
            }
        }

        if (dtoList.isEmpty()) {
            throw new ApiException(ErrorStatus._APPLICATION_NOT_FOUND);
        }

        return dtoList;
    }


    @Override
    public List<LotteryResponse.LotteryHistory> getLotteryHistoryByEmployee(Employee employee) {

        List<Application> applications = applicationQueryService.getApplicationsByEmployee(
                employee);
        

        List<LotteryResponse.LotteryHistory> lotteryHistories = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        for (Application application : applications) {
            List<Lottery> lotteries = lotteryRepository.findByApplicationId(application.getId());

            for (Lottery lottery : lotteries) {
                Recruit recruit = lottery.getRecruit();

                Long recruitId = recruit.getId();
                int applicants = lotteryRepository.findAllApplicationByRecruitId(recruitId).size();
                double competition = (double) applicants / recruit.getRecruitCnt();  // 경쟁률 계산
                String competitionRate = String.format("%.1f : 1", competition);

                LocalDateTime lotteryCreatedAt = lottery.getCreatedAt();
                LocalDateTime recruitEndDt = recruit.getRecruitEndDt();

                if (currentTime.isAfter(recruitEndDt)) {  // 현재 시간이 모집 마감 시간 이후인지 확인
                    LotteryResponse.LotteryHistory history = LotteryResponse.LotteryHistory.builder()
                            .lotteryId(lottery.getId())
                            .childName(lottery.getChildNm())
                            .kindergartenName(recruit.getKindergarten().getKindergartenNm())
                            .ageClass(recruit.getAgeClass())
                            .result(lottery.getResult().name())
                            .applicationDate(lotteryCreatedAt)
                            .competition(competitionRate)
                            .build();

                    lotteryHistories.add(history);
                }
            }
        }

        return lotteryHistories;
    }

}
