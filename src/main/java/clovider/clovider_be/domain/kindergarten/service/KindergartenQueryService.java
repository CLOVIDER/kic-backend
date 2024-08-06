package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import java.util.List;

public interface KindergartenQueryService {

    KindergartenGetResponse getKindergarten(Long kindergartenId);

    List<KindergartenGetResponse> getAllKindergartens();
}
