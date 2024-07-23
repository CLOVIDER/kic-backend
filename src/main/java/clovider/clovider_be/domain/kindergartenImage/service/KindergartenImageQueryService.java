package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.List;

public interface KindergartenImageQueryService {
    List<String> getKindergartenImageUrls(Long KindergartenId);
    List<KindergartenImage> getKindergartenImage(Long kindergartenId);

}
