package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
import java.util.List;

public interface KindergartenClassCommandService {
    List<KindergartenClassDTO> saveKindergartenClass(Kindergarten kindergarten, List<KindergartenClassDTO> kindergartenClasses);

    List<KindergartenClassDTO> updateKindergartenClass(Kindergarten kindergarten, List<KindergartenClassDTO> newKindergartenClass);
}