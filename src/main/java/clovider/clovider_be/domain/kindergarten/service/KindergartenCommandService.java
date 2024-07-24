package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenUpdateResponse;

public interface KindergartenCommandService {

    KindergartenRegisterResponse registerKindergarten(KindergartenRequest.KindergartenRegisterRequest kindergartenRequest);
    CustomResult deleteKindergarten(Long kindergartenId);
    KindergartenUpdateResponse updateKindergarten(Long kindergartenId, KindergartenRequest.KindergartenUpdateRequest kindergartenUpdateRequest);

}
