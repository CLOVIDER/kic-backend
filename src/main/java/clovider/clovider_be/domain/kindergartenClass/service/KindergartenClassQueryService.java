package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassRequest;
import java.util.List;

public interface KindergartenClassQueryService {
    List<KindergartenClassRequest> getKindergartenClass(Long kindergartenId);
}
