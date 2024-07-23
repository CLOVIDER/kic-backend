package clovider.clovider_be.domain.kindergarten.service;

import static clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.toKindergartenGetResponse;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenQueryServiceImpl implements KindergartenQueryService {
    private final KindergartenRepository kindergartenRepository;
    private final KindergartenImageQueryService kindergartenImageQuery;

    @Override
    public KindergartenResponse getKindergarten(Long kindergartenId) {
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        List<String> imageUrls = kindergartenImageQuery.getKindergartenImageUrls(kindergartenId);

        return toKindergartenGetResponse(kindergarten, imageUrls);
    }
}
