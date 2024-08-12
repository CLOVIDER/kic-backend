package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import java.util.List;

public interface KindergartenClassCommandService {
    List<KindergartenClass> saveKindergartenClass(Kindergarten kindergarten, List<KindergartenClass> kindergartenClasses);

    List<KindergartenClass> updateKindergartenClass(Kindergarten kindergarten, List<KindergartenClass> newKindergartenClass);
}