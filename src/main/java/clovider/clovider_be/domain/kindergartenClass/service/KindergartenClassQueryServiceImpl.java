package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import com.amazonaws.services.kms.model.KMSInvalidStateException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenClassQueryServiceImpl implements KindergartenClassQueryService {
    private final KindergartenClassRepository kindergartenClassRepository;

    @Override
    public List<KindergartenClassDTO> getKindergartenClass(Long kindergartenId) {
        List<KindergartenClass> kindergartenClasses = kindergartenClassRepository.findByKindergartenId(kindergartenId);

        if (kindergartenClasses == null || kindergartenClasses.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_ClASS_NOT_FOUND);
        }

        return kindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClassDTO.builder()
                        .className(kindergartenClass.getClassName())
                        .ageClass(kindergartenClass.getAgeClass())
                        .build())
                .toList();
    }
}
