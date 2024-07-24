package clovider.clovider_be.domain.kindergarten.service;


import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.*;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.domain.recruit.service.RecruitCommandService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KindergartenCommandServiceImpl implements KindergartenCommandService {
    private final KindergartenRepository kindergartenRepository;
    private final KindergartenImageCommandService kindergartenImageCommandService;
    private final KindergartenQueryService kindergartenQueryService;
    private final KindergartenImageQueryService kindergartenImageQueryService;
    private final RecruitCommandService recruitCommandService;

    @Override
    public KindergartenRegisterResponse registerKindergarten(
            KindergartenRegisterRequest kindergartenRegisterRequest) {

        Kindergarten kindergarten = Kindergarten.builder()
                .kindergartenNm(kindergartenRegisterRequest.getKindergartenNm())
                .kindergartenAddr(kindergartenRegisterRequest.getKindergartenAddr())
                .kindergartenScale(kindergartenRegisterRequest.getKindergartenScale())
                .kindergartenNo(kindergartenRegisterRequest.getKindergartenNo())
                .kindergartenTime(kindergartenRegisterRequest.getKindergartenTime())
                .kindergartenInfo(kindergartenRegisterRequest.getKindergartenInfo())
                .build();

        kindergarten = kindergartenRepository.save(kindergarten);

        Long kindergartenImageId = kindergartenImageCommandService.saveKindergartenImage(kindergarten, kindergartenRegisterRequest.getKindergartenImage());

        return KindergartenRegisterResponse.toKindergartenRegisterResponse(kindergarten, kindergartenImageId);
    }

    @Override
    public CustomResult deleteKindergarten(Long kindergartenId) {
        List<Long> recruitIds = new ArrayList<>();

        KindergartenGetResponse kindergartenGetResponse = kindergartenQueryService.getKindergarten(kindergartenId);

        recruitIds = recruitCommandService.resetKindergarten(kindergartenId);

        kindergartenRepository.deleteById(kindergartenId);

        return CustomResult.toCustomResult(kindergartenId);
    }

    @Override
    public KindergartenUpdateResponse updateKindergarten(Long kindergartenId,
            KindergartenUpdateRequest request) {
        Long kindergartenImageId = 0L;

        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        Kindergarten savedkindergarten = kindergartenRepository.save(updateFields(request, kindergarten));

        if(request.getKindergartenImage() == null){
            kindergartenImageId = kindergartenImageQueryService.getKindergartenImageId(kindergartenId);
        } else {
            kindergartenImageId = kindergartenImageCommandService.updateKindergartenImage(kindergarten, request.getKindergartenImage());
        }

        return KindergartenUpdateResponse.toKindergartenUpdateResponse(savedkindergarten, kindergartenImageId);
    }

    private Kindergarten updateFields(KindergartenUpdateRequest request, Kindergarten kindergarten) {

        if (request.getKindergartenNm() != null) {
            kindergarten.setKindergartenNm(request.getKindergartenNm());
        }
        if (request.getKindergartenAddr() != null) {
            kindergarten.setKindergartenAddr(request.getKindergartenAddr());
        }
        if (request.getKindergartenScale() != null) {
            kindergarten.setKindergartenScale(request.getKindergartenScale());
        }
        if (request.getKindergartenNo() != null) {
            kindergarten.setKindergartenNo(request.getKindergartenNo());
        }
        if (request.getKindergartenTime() != null) {
            kindergarten.setKindergartenTime(request.getKindergartenTime());
        }
        if (request.getKindergartenInfo() != null) {
            kindergarten.setKindergartenInfo(request.getKindergartenInfo());
        }

        return kindergarten;
    }
}
