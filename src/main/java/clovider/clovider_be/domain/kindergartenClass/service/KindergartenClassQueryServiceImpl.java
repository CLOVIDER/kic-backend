package clovider.clovider_be.domain.kindergartenClass.service;

import clovider.clovider_be.domain.kindergartenClass.KindergartenClass;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassRequest;
import clovider.clovider_be.domain.kindergartenClass.repository.KindergartenClassRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenClassQueryServiceImpl implements KindergartenClassQueryService {
    private final KindergartenClassRepository kindergartenClassRepository;

    @Override
    public List<KindergartenClassRequest> getKindergartenClass(Long kindergartenId) {
        List<KindergartenClass> kindergartenClasses = kindergartenClassRepository.findByKindergartenId(kindergartenId);

        if (kindergartenClasses == null || kindergartenClasses.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_ClASS_NOT_FOUND);
        }

        return kindergartenClasses.stream()
                .map(kindergartenClass -> KindergartenClassRequest.builder()
                        .className(kindergartenClass.getClassName())
                        .ageClass(kindergartenClass.getAgeClass())
                        .build())
                .toList();
    }
}
