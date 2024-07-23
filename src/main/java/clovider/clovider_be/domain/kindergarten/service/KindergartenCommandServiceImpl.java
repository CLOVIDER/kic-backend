package clovider.clovider_be.domain.kindergarten.service;

import static clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.toKindergertenDeleteResponse;
import static clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.toKindergertenUpdateResponse;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KindergartenCommandServiceImpl implements KindergartenCommandService {
    private final KindergartenRepository kindergartenRepository;
    private final KindergartenImageCommandService kindergartenImageCommandService;
    private final KindergartenQueryService kindergartenQueryService;

    @Override
    public KindergartenResponse registerKindergarten(
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

        return toKindergertenDeleteResponse(kindergarten.getId(), kindergartenImageId);
    }

    @Override
    public CustomResult deleteKindergarten(Long kindergartenId) {

        kindergartenQueryService.getKindergarten(kindergartenId);

        kindergartenRepository.deleteById(kindergartenId);

        return CustomResult.toCustomResult(kindergartenId);
    }

    @Override
    public KindergartenResponse updateKindergarten(Long kindergartenId,
            KindergartenUpdateRequest request) {
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        Kindergarten savedkindergarten = kindergartenRepository.save(updateFields(request, kindergarten));

        Long kindergartenImageId = 0L;
        if (request.getKindergartenImage() != null) {
            kindergartenImageId = kindergartenImageCommandService.updateKindergartenImage(kindergarten, request.getKindergartenImage());
        }

        return toKindergertenUpdateResponse(savedkindergarten, kindergartenImageId);
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
