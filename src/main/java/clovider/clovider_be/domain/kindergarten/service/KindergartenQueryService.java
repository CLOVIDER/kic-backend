package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;

public interface KindergartenQueryService {

    KindergartenResponse getKindergarten(Long kindergartenId);

}
