package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateResponseDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.util.ArrayList;
import java.util.List;
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
        Kindergarten kindergarten = kindergartenRepository.findById(requestDTO.getKindergartenId())
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        Recruit recruit = Recruit.createRecruit(requestDTO, kindergarten);
        Recruit savedRecruit = recruitRepository.save(recruit);

        return createRecruitCreationInfo(savedRecruit);
    }

    @Override
    @Transactional
    public AdminResponse.RecruitCreationInfo updateRecruit(Long recruitId, RecruitCreateRequestDTO requestDTO) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new ApiException(ErrorStatus._RECRUIT_NOT_FOUND));

        // 업데이트 로직
        recruit.updateRecruit(requestDTO);

        // 저장 및 반환
        Recruit savedRecruit = recruitRepository.save(recruit);
        return createRecruitCreationInfo(savedRecruit);
    }

    private AdminResponse.RecruitCreationInfo createRecruitCreationInfo(Recruit recruit) {
        RecruitResponse.RecruitDateAndWeightInfo recruitDateAndWeightInfo = toRecruitDateAndWeightInfo(recruit);
        AdminResponse.KindergartenClassInfo kindergartenClassInfo = createKindergartenClassInfo(recruit.getKindergarten().getKindergartenNm(), List.of(recruit));

        return AdminResponse.RecruitCreationInfo.builder()
                .kindergartenClassInfoList(List.of(kindergartenClassInfo))
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
