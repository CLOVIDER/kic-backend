package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.Optional;

public interface KindergartenImageQueryService {
    String getKindergartenImageUrls(Long KindergartenId);
    Optional<KindergartenImage> getKindergartenImage(Long kindergartenId);
    Long getKindergartenImageId(Long kindergartenId);
}
