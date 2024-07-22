package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;

public interface KindergartenImageCommandService {
    Long saveKindergartenImage(Kindergarten kindergarten, String kindergartenImage);

}
