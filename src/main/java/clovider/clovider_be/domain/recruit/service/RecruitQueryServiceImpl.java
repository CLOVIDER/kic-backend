package clovider.clovider_be.domain.recruit.service;

import static clovider.clovider_be.domain.recruit.dto.RecruitResponse.toRecruitDateAndWeightInfo;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.KindergartenClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitCreationInfo;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public NowRecruits getNowRecruitOrderByClass(LocalDateTime now) {

        List<NowRecruit> recruits = recruitRepository.findNowRecruitOrderByClass(now);

        return new NowRecruits(recruits);
    }

    @Override
    @Cacheable(value = "recruit", key = "'ing'")
    public List<Long> getRecruitIngAndScheduled(LocalDateTime now) {
        return recruitRepository.findRecruitIngAndScheduled(now);
    }

    @Override
    public List<Recruit> getNowRecruit(LocalDateTime now) {
        return recruitRepository.findNowRecruit(now);
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
    public List<Recruit> getRecruitResultWithRecruitAndKindergarten() {
        return recruitRepository.findRecruitResult(LocalDateTime.now());
    }

    @Override
    public RecruitCreationInfo getRecruitCreationInfo() {
        List<Recruit> nowRecruit = getRecruitAndKindergarten();

        // 현재 생성돼있는 모집이 없을 시
        if (nowRecruit.isEmpty()) {
            return createEmptyRecruitCreationInfo();
        }

        return createRecruitCreationInfoForNonEmpty(nowRecruit);
    }

    private RecruitCreationInfo createEmptyRecruitCreationInfo() {
        // 빈 응답 생성 메소드를 DTO 클래스로 분리
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = RecruitDateAndWeightInfo.createEmpty();
        KindergartenClassInfo emptyKindergartenClassInfo = KindergartenClassInfo.createEmpty();
        List<KindergartenClassInfo> kindergartenClassInfoList = Collections.singletonList(
                emptyKindergartenClassInfo);

        return AdminResponse.toRecruitCreationInfo(kindergartenClassInfoList,
                recruitDateAndWeightInfo, false);
    }

    private RecruitCreationInfo createRecruitCreationInfoForNonEmpty(List<Recruit> nowRecruit) {
        // 기존 응답 생성 로직
        Map<String, List<Recruit>> groupByKindergarten = nowRecruit.stream()
                .collect(Collectors.groupingBy(
                        recruit -> recruit.getKindergarten().getKindergartenNm()));

        RecruitDateAndWeightInfo recruitDateAndWeightInfo = getRecruitDateAndWeightInfo(
                nowRecruit.get(0).getId());

        List<KindergartenClassInfo> kindergartenClassInfoList = groupByKindergarten.entrySet()
                .stream()
                .map(entry -> createKindergartenClassInfo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return AdminResponse.toRecruitCreationInfo(kindergartenClassInfoList,
                recruitDateAndWeightInfo, true);

    }

    private RecruitDateAndWeightInfo getRecruitDateAndWeightInfo(Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND));
        return toRecruitDateAndWeightInfo(recruit);
    }

    private KindergartenClassInfo createKindergartenClassInfo(String kindergartenName,
            List<Recruit> recruits) {
        // 모집 별 분반 정보 변환
        List<RecruitClassInfo> classInfos = recruits.stream()
                .map(AdminResponse::toRecruitClassInfo)
                .collect(Collectors.toList());

        return KindergartenClassInfo.builder()
                .kindergartenName(kindergartenName)
                .classInfoList(classInfos)
                .build();
    }

    @Override
    public Optional<Recruit> getRecruitByKindergarten(Kindergarten kindergarten, Integer ageClass) {
        return recruitRepository.findByKindergartenAndAgeClass(kindergarten, ageClass);
    }
}
