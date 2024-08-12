package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
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

    @Override
    public List<KindergartenClassDTO> saveKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClassDTO> kindergartenClasses) {

        List<KindergartenClass> newKindergartenClasses = kindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClass.builder()
                        .kindergarten(kindergarten)
                        .ageClass(kindergartenClass.getAgeClass())
                        .className(kindergartenClass.getClassName())
                        .build())
                .toList();

        List<KindergartenClass> savedClasses = kindergartenClassRepository.saveAll(newKindergartenClasses);

        return savedClasses.stream()
                .map(savedClass -> KindergartenClassDTO.builder()
                        .className(savedClass.getClassName())
                        .ageClass(savedClass.getAgeClass())
                        .build())
                .toList();
    }

    @Override
    public List<KindergartenClassDTO> updateKindergartenClass(Kindergarten kindergarten,
            List<KindergartenClassDTO> newKindergartenClasses) {

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
                .map(savedClass -> KindergartenClassDTO.builder()
                        .className(savedClass.getClassName())
                        .ageClass(savedClass.getAgeClass())
                        .build())
                .toList();
    }
}
