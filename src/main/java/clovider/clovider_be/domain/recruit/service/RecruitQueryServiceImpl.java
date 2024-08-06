package clovider.clovider_be.domain.recruit.service;

import static clovider.clovider_be.domain.recruit.dto.RecruitResponse.toRecruitDateAndWeightInfo;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.KindergartenClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitCreationInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Override
    public RecruitCreationInfo getRecruitCreationInfo() {
        List<Recruit> nowRecruit = getRecruitAndKindergarten();

        // 어린이집 이름으로 그룹핑 - 어린이집 별 모집 리스트 조회
        Map<String, List<Recruit>> groupByKindergarten = nowRecruit.stream()
                .collect(Collectors.groupingBy(recruit -> recruit.getKindergarten().getKindergartenNm()));

        // 진행중인 모집들의 기간 정보 및 가중치 설정은 동일하므로 첫번째 모집의 정보를 조회
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = getRecruitDateAndWeightInfo(nowRecruit.get(0).getId());

        List<KindergartenClassInfo> kindergartenClassInfoList = groupByKindergarten.entrySet().stream()
                .map(entry -> createKindergartenClassInfo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return AdminResponse.toRecruitCreationInfo(kindergartenClassInfoList, recruitDateAndWeightInfo);
    }

    private RecruitDateAndWeightInfo getRecruitDateAndWeightInfo(Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND));
        return toRecruitDateAndWeightInfo(recruit);
    }

    private KindergartenClassInfo createKindergartenClassInfo(String kindergartenName, List<Recruit> recruits) {
        // 모집 별 분반 정보 변환
        List<RecruitClassInfo> classInfos = recruits.stream()
                .map(AdminResponse::toRecruitClassInfo)
                .collect(Collectors.toList());

        return KindergartenClassInfo.builder()
                .kindergartenName(kindergartenName)
                .classInfoList(classInfos)
                .build();
    }


}
