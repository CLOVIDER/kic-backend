package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.List;

public interface KindergartenClassQueryService {
    List<KindergartenClass> getKindergartenClass(Long kindergartenId);
}
