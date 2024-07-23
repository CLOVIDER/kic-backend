package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;

public interface KindergartenCommandService {

    KindergartenResponse registerKindergarten(KindergartenRequest.KindergartenRegisterRequest kindergartenRequest);
    CustomResult deleteKindergarten(Long kindergartenId);
    KindergartenResponse updateKindergarten(Long kindergartenId, KindergartenRequest.KindergartenUpdateRequest kindergartenUpdateRequest);

}
