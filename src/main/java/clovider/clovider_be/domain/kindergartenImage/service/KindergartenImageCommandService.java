package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import java.util.List;

public interface KindergartenImageCommandService {
    List<Long> saveKindergartenImage(Kindergarten kindergarten, List<String> kindergartenImage);

    List<Long> updateKindergartenImage(Kindergarten kindergarten, List<String> newImageUrl);
}