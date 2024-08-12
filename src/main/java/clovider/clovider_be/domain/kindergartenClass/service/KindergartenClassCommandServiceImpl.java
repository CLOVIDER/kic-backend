package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KindergartenClassCommandServiceImpl implements KindergartenClassCommandService {

    private final KindergartenClassRepository kindergartenClassRepository;
    private final KindergartenClassQueryService kindergartenClassQueryService;

    @Override
    public List<KindergartenClass> saveKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClass> kindergartenClasses) {

        List<KindergartenClass> newKindergartenClasses = kindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClass.builder()
                        .kindergarten(kindergarten)
                        .ageClass(kindergartenClass.getAgeClass())
                        .className(kindergartenClass.getClassName())
                        .build())
                .collect(Collectors.toList());

        return kindergartenClassRepository.saveAll(newKindergartenClasses);
    }

    @Override
    public List<KindergartenClass> updateKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClass> newKindergartenClasses) {

        List<KindergartenClass> existingClasses = kindergartenClassQueryService.getKindergartenClass(kindergarten.getId());

        existingClasses.clear();

        for (KindergartenClass newClass : newKindergartenClasses) {
            KindergartenClass kindergartenClass = KindergartenClass.builder()
                    .kindergarten(kindergarten)
                    .ageClass(newClass.getAgeClass())
                    .className(newClass.getClassName())
                    .build();
            existingClasses.add(kindergartenClass);
        }

        return kindergartenClassRepository.saveAll(existingClasses);
    }
}
