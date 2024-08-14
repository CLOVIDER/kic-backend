package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminRequest;
import clovider.clovider_be.domain.admin.dto.AdminRequest.KindergartenClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitCreationRequest;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.service.KindergartenQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService {

    private final RecruitQueryService recruitQueryService;
    private final RecruitRepository recruitRepository;
    private final KindergartenQueryService kindergartenQueryService;

    @Override
    public List<Long> resetKindergarten(Long kindergartenId) {
        List<Recruit> recruits = recruitQueryService.getRecruitByKindergarten(kindergartenId);

        // 5가 디폴트 킨더가든 값임
        Kindergarten kindergarten = kindergartenQueryService.getKindergartenOnly(5L);

        List<Long> recruitIds = new ArrayList<>();

        for (Recruit recruit : recruits) {
            recruit.changeKindergarten(kindergarten);
            recruitRepository.save(recruit);
            recruitIds.add(recruit.getId());
        }

        return recruitIds;
    }


    @Override
    public String updateRecruit(RecruitCreationRequest request, Long recruitId) {

        List<AdminRequest.KindergartenClassInfo> kindergartenClassInfoList = request.getKindergartenClassInfoList();
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = request.getRecruitDateAndWeightInfo();

        // 요청한 어린이집 개수만큼 반복
        for (KindergartenClassInfo kindergartenClassInfo : kindergartenClassInfoList) {
            String kindergartenName = kindergartenClassInfo.getKindergartenName();
            List<AdminRequest.RecruitClassInfo> classInfoList = kindergartenClassInfo.getClassInfoList();

            Kindergarten kindergarten = kindergartenQueryService.getKindergartenByName(
                    kindergartenName);

            // 요청한 분반 개수만큼 반복
            for (RecruitClassInfo classInfo : classInfoList) {

                Recruit existingRecruit = recruitQueryService.getRecruitByKindergarten(
                        kindergarten, classInfo.getAgeClass());

                // 기존 모집 정보 업데이트
                existingRecruit.updateRecruitDetails(classInfo, recruitDateAndWeightInfo);
            }
        }

        return "모집을 정상적으로 수정하였습니다.";
    }

    @Override
    public String createRecruit(RecruitCreationRequest request) {
        List<AdminRequest.KindergartenClassInfo> kindergartenClassInfoList = request.getKindergartenClassInfoList();
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = request.getRecruitDateAndWeightInfo();

        // 요청한 어린이집 개수만큼 반복
        for (AdminRequest.KindergartenClassInfo kindergartenClassInfo : kindergartenClassInfoList) {
            String kindergartenName = kindergartenClassInfo.getKindergartenName();
            List<AdminRequest.RecruitClassInfo> classInfoList = kindergartenClassInfo.getClassInfoList();

            // 어린이집 이름으로 kindergarten 조회
            Kindergarten kindergartenByName = kindergartenQueryService.getKindergartenByName(
                    kindergartenName);

            // 요청한 분반 개수만큼 반복
            for (AdminRequest.RecruitClassInfo classInfo : classInfoList) {

                // 개별 모집 생성
                recruitRepository.save(Recruit.createRecruit(classInfo, recruitDateAndWeightInfo,
                        kindergartenByName));
            }
        }

        return "모집을 정상적으로 생성하였습니다.";

    }

}
