package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.lottery.Lottery;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
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
    public Page<LotteryResult> getLotteryResultByLotteryId(AgeClass ageClass, Long kindergartenId, Pageable pageable, String value) {
        return lotteryRepository.getLotteryResults(ageClass, kindergartenId,pageable,value);
    }

    @Override
    public List<LotteryResultsGroupedByChildDTO> getLotteryResultsByEmployeeId(Employee employee) {
        if (employee == null) {
            throw new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND);
        }

        List<Application> applications = applicationRepository.findAllByEmployee(employee);

        if (applications.isEmpty()) {
            throw new ApiException(ErrorStatus._APPLICATION_NOT_FOUND);
        }

        Map<String, List<LotteryResultByEmployeeDTO>> groupedResults = applications.stream()
                .flatMap(application -> lotteryRepository.findByApplicationId(application.getId()).stream())
                .map(lottery -> LotteryResultByEmployeeDTO.builder()
                        .applicationId(lottery.getApplication().getId())
                        .recruitId(lottery.getRecruit().getId())
                        .childName(lottery.getChildNm())
                        .kindergartenName(lottery.getRecruit().getKindergarten().getKindergartenNm())
                        .ageClass(lottery.getRecruit().getAgeClass())
                        .result(lottery.getResult().name())
                        .waitingNumber(lottery.getResult() == Result.LOSE ? lottery.getRankNo() : null)
                        .build())
                .collect(Collectors.groupingBy(LotteryResultByEmployeeDTO::getChildName));

        return groupedResults.entrySet().stream()
                .map(entry -> LotteryResultsGroupedByChildDTO.builder()
                        .childName(entry.getKey())
                        .lotteryResults(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }


}
