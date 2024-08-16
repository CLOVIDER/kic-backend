package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassRequest;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KindergartenClassCommandServiceImpl implements KindergartenClassCommandService {

    private final KindergartenClassRepository kindergartenClassRepository;

    @Override
    public List<KindergartenClassRequest> saveKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClassRequest> kindergartenClasses) {

        List<KindergartenClass> newKindergartenClasses = kindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClass.builder()
                        .kindergarten(kindergarten)
                        .ageClass(kindergartenClass.getAgeClass())
                        .className(kindergartenClass.getClassName())
                        .build())
                .toList();

        List<KindergartenClass> savedClasses = kindergartenClassRepository.saveAll(newKindergartenClasses);

        return savedClasses.stream()
                .map(savedClass -> KindergartenClassRequest.builder()
                        .className(savedClass.getClassName())
                        .ageClass(savedClass.getAgeClass())
                        .build())
                .toList();
    }

    @Override
    public List<KindergartenClassRequest> updateKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClassRequest> newKindergartenClasses) {

        if(newKindergartenClasses == null || newKindergartenClasses.isEmpty()){
            List<KindergartenClass> existingClasses = kindergartenClassRepository.findByKindergartenId(kindergarten.getId());
            return existingClasses.stream()
                    .map(savedClass -> KindergartenClassRequest.builder()
                            .className(savedClass.getClassName())
                            .ageClass(savedClass.getAgeClass())
                            .build())
                    .toList();
        }

        kindergartenClassRepository.deleteAllByKindergartenId(kindergarten.getId());

        List<KindergartenClass> updateKindergartenClasses = newKindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClass.builder()
                        .kindergarten(kindergarten)
                        .ageClass(kindergartenClass.getAgeClass())
                        .className(kindergartenClass.getClassName())
                        .build())
                .toList();

        List<KindergartenClass> savedClasses = kindergartenClassRepository.saveAll(updateKindergartenClasses);

        return savedClasses.stream()
                .map(savedClass -> KindergartenClassRequest.builder()
                        .className(savedClass.getClassName())
                        .ageClass(savedClass.getAgeClass())
                        .build())
                .toList();
    }
}
