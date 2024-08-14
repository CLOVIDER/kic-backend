package clovider.clovider_be.domain.recruit.service;

import static clovider.clovider_be.domain.enums.AgeClass.fromDescription;

import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitCreationRequest;
import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.admin.dto.AdminResponse.KindergartenClassInfo;
import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitClassInfo;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergarten.service.KindergartenQueryService;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
import clovider.clovider_be.domain.recruit.dto.RecruitResponseDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitUpdateRequestDTO;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService {

    private final RecruitQueryService recruitQueryService;
    private final RecruitRepository recruitRepository;
    private final KindergartenRepository kindergartenRepository;
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
    public String updateRecruit(RecruitCreationRequest request) {
        List<KindergartenClassInfo> kindergartenClassInfoList = request.getKindergartenClassInfoList();
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = request.getRecruitDateAndWeightInfo();

        // 요청한 어린이집 개수만큼 반복
        for (KindergartenClassInfo kindergartenClassInfo : kindergartenClassInfoList) {
            String kindergartenName = kindergartenClassInfo.getKindergartenName();
            List<RecruitClassInfo> classInfoList = kindergartenClassInfo.getClassInfoList();

            // 어린이집 이름으로 kindergarten 조회
            Kindergarten kindergartenByName = kindergartenQueryService.getKindergartenByName(kindergartenName);

            // 요청한 분반 개수만큼 반복
            for (RecruitClassInfo classInfo : classInfoList) {

                // AgeClass의 Description으로 온 값을 변환
                String ageClassDescription = classInfo.getAgeClass();
                AgeClass ageClass = fromDescription(ageClassDescription);

                // 기존 Recruit 조회
                Recruit existingRecruit = recruitRepository.findByKindergartenAndAgeClass(kindergartenByName, ageClass)
                        .orElseThrow(() -> new EntityNotFoundException("해당하는 모집 정보를 찾을 수 없습니다."));

                // 기존 모집 정보 업데이트
                existingRecruit.updateRecruitDetails(classInfo, recruitDateAndWeightInfo);

                // 변경된 모집 정보 저장
                recruitRepository.save(existingRecruit);
            }
        }

        return "모집을 정상적으로 수정하였습니다.";
    }



    @Override
    public String createRecruit(RecruitCreationRequest request) {
        List<KindergartenClassInfo> kindergartenClassInfoList = request.getKindergartenClassInfoList();
        RecruitDateAndWeightInfo recruitDateAndWeightInfo = request.getRecruitDateAndWeightInfo();

        // 요청한 어린이집 개수만큼 반복
        for (KindergartenClassInfo kindergartenClassInfo : kindergartenClassInfoList) {
            String kindergartenName = kindergartenClassInfo.getKindergartenName();
            List<RecruitClassInfo> classInfoList = kindergartenClassInfo.getClassInfoList();

            // 어린이집 이름으로 kindergarten 조회
            Kindergarten kindergartenByName = kindergartenQueryService.getKindergartenByName(
                    kindergartenName);

            // 요청한 분반 개수만큼 반복
            for (RecruitClassInfo classInfo : classInfoList) {

                // AgeClass의 Description으로 온 값을 변환
                String ageClassDescription = classInfo.getAgeClass();
                AgeClass ageClass = fromDescription(ageClassDescription);

                // 개별 모집 생성
                recruitRepository.save(Recruit.createRecruit(classInfo
                        ,recruitDateAndWeightInfo,kindergartenByName,ageClass));
            }
        }

        return "모집을 정상적으로 생성하였습니다.";

    }

}
