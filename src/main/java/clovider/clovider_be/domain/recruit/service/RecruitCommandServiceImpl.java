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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService{
    private final RecruitQueryService recruitQueryService;
    private final RecruitRepository recruitRepository;
    private final KindergartenRepository kindergartenRepository;
    private final KindergartenQueryService kindergartenQueryService;

    @Override
    public List<Long> resetKindergarten(Long kindergartenId) {
        List<Recruit> recruits = recruitQueryService.getRecruitByKindergarten(kindergartenId);
        List<Long> recruitIds = new ArrayList<>();

        for (Recruit recruit : recruits) {
            recruit.changeKindergarten(null);
            recruitRepository.save(recruit);
            recruitIds.add(recruit.getId());
        }

        return recruitIds;
    }



    @Override
    public RecruitResponseDTO updateRecruit(RecruitUpdateRequestDTO requestDTO) {

        List<Recruit> recruits = recruitRepository.findAllById(requestDTO.getRecruitIds());

        //모집 정보가 존재하는지 확인
        if (recruits.size() != requestDTO.getRecruitIds().size()) {
            throw new ApiException(ErrorStatus._RECRUIT_NOT_FOUND);
        }


        // 각 모집 정보 한 번에 업데이트
        for (Recruit recruit : recruits) {
            recruit.updateRecruitDetails(requestDTO);
        }

        recruitRepository.saveAll(recruits);

        RecruitResponseDTO.Result result = new RecruitResponseDTO.Result();
        result.setCreatedAt(recruits.get(0).getCreatedAt());

        RecruitResponseDTO responseDTO = new RecruitResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setCode("SUCCESS");
        responseDTO.setMessage("모집 정보가 성공적으로 업데이트되었습니다.");
        responseDTO.setResult(result);

        return responseDTO;
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
