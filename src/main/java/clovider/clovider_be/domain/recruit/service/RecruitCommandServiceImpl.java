package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.*;
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
        // 다수의 모집 정보 조회
        List<Recruit> recruits = recruitRepository.findAllById(requestDTO.getRecruitIds());

        if (recruits.size() != requestDTO.getRecruitIds().size()) {
            throw new ApiException(ErrorStatus._RECRUIT_NOT_FOUND);
        }

        // 어린이집 정보 조회 (가정: 모든 recruit가 동일한 kindergarten에 속함)
        // 만약 여러 kindergarten을 처리해야 한다면 이 부분을 조정해야 합니다.
        Long kindergartenId = recruits.get(0).getKindergarten().getId();
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        // 업데이트 로직
        for (Recruit recruit : recruits) {
            recruit.setAgeClass(requestDTO.getAgeClass());
            recruit.setRecruitStartDt(requestDTO.getRecruitStartDt());
            recruit.setRecruitEndDt(requestDTO.getRecruitEndDt());
            recruit.setRecruitCnt(requestDTO.getRecruitCnt());
            recruit.setFirstStartDt(requestDTO.getFirstStartDt());
            recruit.setFirstEndDt(requestDTO.getFirstEndDt());
            recruit.setSecondStartDt(requestDTO.getSecondStartDt());
            recruit.setSecondEndDt(requestDTO.getSecondEndDt());

            // 가중치 정보 업데이트
            recruit.setWorkYearsUsage(requestDTO.getRecruitWeightInfo().getWorkYearsUsage());
            recruit.setIsSingleParentUsage(requestDTO.getRecruitWeightInfo().getIsSingleParentUsage());
            recruit.setChildrenCntUsage(requestDTO.getRecruitWeightInfo().getChildrenCntUsage());
            recruit.setIsDisabilityUsage(requestDTO.getRecruitWeightInfo().getIsDisabilityUsage());
            recruit.setIsDualIncomeUsage(requestDTO.getRecruitWeightInfo().getIsDualIncomeUsage());
            recruit.setIsEmployeeCoupleUsage(requestDTO.getRecruitWeightInfo().getIsEmployeeCoupleUsage());
            recruit.setIsSiblingUsage(requestDTO.getRecruitWeightInfo().getIsSiblingUsage());
        }

        recruitRepository.saveAll(recruits);

        // 반환할 DTO 생성
        RecruitResponseDTO.Result result = new RecruitResponseDTO.Result();
        result.setId(recruits.get(0).getId()); // 첫 번째 모집의 ID를 설정
        result.setCreatedAt(recruits.get(0).getCreatedAt()); // 첫 번째 모집의 생성일시를 설정

        RecruitResponseDTO responseDTO = new RecruitResponseDTO();
        responseDTO.setSuccess(true);
        responseDTO.setCode("SUCCESS"); // 성공 코드 설정 (필요에 따라 수정)
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
