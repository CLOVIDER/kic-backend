package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassRequest;
import java.util.List;

public interface KindergartenClassCommandService {
    List<KindergartenClassRequest> saveKindergartenClass(Kindergarten kindergarten, List<KindergartenClassRequest> kindergartenClasses);

    List<KindergartenClassRequest> updateKindergartenClass(Kindergarten kindergarten, List<KindergartenClassRequest> newKindergartenClass);
}