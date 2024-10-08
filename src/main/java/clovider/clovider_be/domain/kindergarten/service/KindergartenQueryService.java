package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import java.util.List;

public interface KindergartenQueryService {

    KindergartenGetResponse getKindergarten(Long kindergartenId);

    Kindergarten getKindergartenOnly(Long kindergartenId);

    List<KindergartenGetResponse> getAllKindergartens();

    Kindergarten getKindergartenByName(String kindergartenName);
}
