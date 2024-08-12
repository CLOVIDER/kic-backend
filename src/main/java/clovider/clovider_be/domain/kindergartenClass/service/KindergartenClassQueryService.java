package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import java.util.List;

public interface KindergartenClassQueryService {
    List<KindergartenClassDTO> getKindergartenClass(Long kindergartenId);
}
