package clovider.clovider_be.domain.kindergarten.service;


import static clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse.toKindergartenRegisterResponse;
import static clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenDeleteResponse.toKindergartenDeleteResponse;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.*;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassRequest;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassResponse;
import clovider.clovider_be.domain.kindergartenClass.service.KindergartenClassCommandService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.domain.recruit.service.RecruitCommandService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    private final KindergartenClassCommandService kindergartenClassCommandService;
    private final RecruitCommandService recruitCommandService;

    @Override
    public KindergartenRegisterResponse registerKindergarten(
            KindergartenRegisterRequest kindergartenRegisterRequest) {

        Kindergarten kindergarten = Kindergarten.builder()
                .kindergartenNm(kindergartenRegisterRequest.getKindergartenNm())
                .kindergartenAddr(kindergartenRegisterRequest.getKindergartenAddr())
                .kindergartenScale(kindergartenRegisterRequest.getKindergartenScale())
                .kindergartenCapacity(kindergartenRegisterRequest.getKindergartenCapacity())
                .kindergartenNo(kindergartenRegisterRequest.getKindergartenNo())
                .kindergartenTime(kindergartenRegisterRequest.getKindergartenTime())
                .kindergartenInfo(kindergartenRegisterRequest.getKindergartenInfo())
                .build();


        kindergarten = kindergartenRepository.save(kindergarten);

        List<KindergartenClassRequest> kindergartenClasses = kindergartenClassCommandService.saveKindergartenClass(kindergarten, kindergartenRegisterRequest.getKindergartenClass());

        List<KindergartenClassResponse> newKindergartenClasses = kindergartenClasses.stream()
                .map(req -> KindergartenClassResponse.builder()
                        .className(req.getClassName())
                        .ageClassString(req.getAgeClass() + "세")
                        .build())
                .collect(Collectors.toList());

        List<Long> kindergartenImageIds = kindergartenImageCommandService.saveKindergartenImage(kindergarten, kindergartenRegisterRequest.getKindergartenImageUrls());

        return toKindergartenRegisterResponse(kindergarten, newKindergartenClasses, kindergartenImageIds);
    }

    @Override
    public KindergartenDeleteResponse deleteKindergarten(Long kindergartenId) {

        List<Long> recruitIds = new ArrayList<>();

        kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));
        
        recruitIds = recruitCommandService.resetKindergarten(kindergartenId);

        kindergartenRepository.deleteById(kindergartenId);

        return toKindergartenDeleteResponse(kindergartenId, recruitIds);
    }

    @Override
    public KindergartenUpdateResponse updateKindergarten(Long kindergartenId, KindergartenUpdateRequest request) {

        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        kindergarten.updateKindergarten(
                request.getKindergartenNm(),
                request.getKindergartenAddr(),
                request.getKindergartenScale(),
                request.getKindergartenCapacity(),
                request.getKindergartenNo(),
                request.getKindergartenTime(),
                request.getKindergartenInfo());

        Kindergarten savedKindergarten = kindergartenRepository.save(kindergarten);

        List<Long> kindergartenImageIds = kindergartenImageCommandService.updateKindergartenImage(kindergarten, request.getKindergartenImageUrls());

        List<KindergartenClassRequest> kindergartenClasses = kindergartenClassCommandService.updateKindergartenClass(kindergarten, request.getKindergartenClass());

        List<KindergartenClassResponse> newKindergartenClasses = kindergartenClasses.stream()
                .map(req -> KindergartenClassResponse.builder()
                        .className(req.getClassName())
                        .ageClassString(req.getAgeClass() + "세")
                        .build())
                .collect(Collectors.toList());

        return KindergartenUpdateResponse.toKindergartenUpdateResponse(savedKindergarten, newKindergartenClasses, kindergartenImageIds);
    }
}
