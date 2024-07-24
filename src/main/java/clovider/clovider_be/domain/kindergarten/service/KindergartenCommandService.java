package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenUpdateResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenDeleteResponse;

public interface KindergartenCommandService {

    KindergartenRegisterResponse registerKindergarten(KindergartenRegisterRequest kindergartenRequest);
    KindergartenDeleteResponse deleteKindergarten(Long kindergartenId);
    KindergartenUpdateResponse updateKindergarten(Long kindergartenId, KindergartenUpdateRequest kindergartenUpdateRequest);

}
