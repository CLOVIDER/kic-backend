package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;

public interface KindergartenQueryService {

    KindergartenGetResponse getKindergarten(Long kindergartenId);

}
