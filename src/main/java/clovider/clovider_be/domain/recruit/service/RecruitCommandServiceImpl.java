package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.*;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateResponseDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static clovider.clovider_be.domain.recruit.dto.RecruitResponse.toRecruitDateAndWeightInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService{
    private final RecruitQueryService recruitQueryService;
    private final RecruitRepository recruitRepository;
    private final KindergartenRepository kindergartenRepository;

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
    @Transactional
    public AdminResponse.RecruitCreationInfo createRecruit(RecruitCreateRequestDTO requestDTO) {
        List<Recruit> allRecruits = new ArrayList<>();

        for (RecruitCreateRequestDTO.KindergartenRecruitRequest kindergartenRequest : requestDTO.getKindergartens()) {
            Kindergarten kindergarten = kindergartenRepository.findById(kindergartenRequest.getKindergartenId())
                    .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

            for (RecruitCreateRequestDTO.RecruitClassCreateRequestDTO classDTO : kindergartenRequest.getClasses()) {
                Recruit recruit = Recruit.createRecruit(classDTO, kindergarten);
                allRecruits.add(recruit);
                recruitRepository.save(recruit);
            }
        }

        return createRecruitCreationInfo(allRecruits);
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

    private AdminResponse.RecruitCreationInfo createRecruitCreationInfo(List<Recruit> recruits) {
        // 어린이집별로 Recruit를 그룹화
        Map<String, List<Recruit>> recruitsByKindergarten = recruits.stream()
                .collect(Collectors.groupingBy(recruit -> recruit.getKindergarten().getKindergartenNm()));

        // 그룹화된 데이터를 기반으로 KindergartenClassInfo 생성
        List<AdminResponse.KindergartenClassInfo> kindergartenClassInfos = recruitsByKindergarten.entrySet().stream()
                .map(entry -> {
                    String kindergartenName = entry.getKey();
                    List<AdminResponse.RecruitClassInfo> classInfos = entry.getValue().stream()
                            .map(this::toRecruitClassInfo)
                            .collect(Collectors.toList());

                    return AdminResponse.KindergartenClassInfo.builder()
                            .kindergartenName(kindergartenName)
                            .classInfoList(classInfos)
                            .build();
                })
                .collect(Collectors.toList());

        // 모집 기간 상세 및 가중치 설정 정보
        RecruitResponse.RecruitDateAndWeightInfo recruitDateAndWeightInfo = toRecruitDateAndWeightInfo(recruits.get(0));

        return AdminResponse.RecruitCreationInfo.builder()
                .kindergartenClassInfoList(kindergartenClassInfos)
                .recruitDateAndWeightInfo(recruitDateAndWeightInfo)
                .isCreated(true)
                .build();
    }


    private AdminResponse.KindergartenClassInfo createKindergartenClassInfo(String kindergartenName, List<Recruit> recruits) {
        List<AdminResponse.RecruitClassInfo> classInfos = recruits.stream()
                .map(this::toRecruitClassInfo)
                .collect(Collectors.toList());

        return AdminResponse.KindergartenClassInfo.builder()
                .kindergartenName(kindergartenName)
                .classInfoList(classInfos)
                .build();
    }

    private AdminResponse.RecruitClassInfo toRecruitClassInfo(Recruit recruit) {
        return AdminResponse.RecruitClassInfo.builder()
                .ageClass(recruit.getAgeClass().getDescription())
                .recruitCnt(recruit.getRecruitCnt())
                .build();
        }



    private RecruitResponse.RecruitDateAndWeightInfo toRecruitDateAndWeightInfo(Recruit recruit) {
        RecruitResponse.RecruitDateInfo recruitDateInfo = RecruitResponse.RecruitDateInfo.builder()
                .recruitStartDt(recruit.getRecruitStartDt())
                .recruitEndDt(recruit.getRecruitEndDt())
                .firstStartDt(recruit.getFirstStartDt())
                .firstEndDt(recruit.getFirstEndDt())
                .secondStartDt(recruit.getSecondStartDt())
                .secondEndDt(recruit.getSecondEndDt())
                .build();

        RecruitResponse.RecruitWeightInfo recruitWeightInfo = RecruitResponse.RecruitWeightInfo.builder()
                .workYearsUsage(recruit.getWorkYearsUsage())
                .isSingleParentUsage(recruit.getIsSingleParentUsage())
                .childrenCntUsage(recruit.getChildrenCntUsage())
                .isDisabilityUsage(recruit.getIsDisabilityUsage())
                .isDualIncomeUsage(recruit.getIsDualIncomeUsage())
                .isEmployeeCoupleUsage(recruit.getIsEmployeeCoupleUsage())
                .isSiblingUsage(recruit.getIsSiblingUsage())
                .build();

        return RecruitResponse.RecruitDateAndWeightInfo.builder()
                .recruitDateInfo(recruitDateInfo)
                .recruitWeightInfo(recruitWeightInfo)
                .build();
    }





}
