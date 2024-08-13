package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenImageQueryServiceImpl implements KindergartenImageQueryService {
    private final KindergartenImageRepository kindergartenImageRepository;

    @Override
    public List<String> getKindergartenImageUrls(Long kindergartenId) {
        List<KindergartenImage> kindergartenImages = kindergartenImageRepository.findByKindergartenId(kindergartenId);

        if (kindergartenImages == null || kindergartenImages.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_IMAGE_NOT_FOUND);
        }

        return kindergartenImages.stream()
                .map(KindergartenImage::getImage)
                .collect(Collectors.toList());
    }


    @Override
    public List<KindergartenImage> getKindergartenImage(Long kindergartenId) {
        List<KindergartenImage> kindergartenImages = kindergartenImageRepository.findByKindergartenId(kindergartenId);

        if (kindergartenImages == null || kindergartenImages.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_IMAGE_NOT_FOUND);
        }

        return kindergartenImages;
    }

    @Override
    public List<Long> getKindergartenImageId(Long kindergartenId) {
        List<KindergartenImage> kindergartenImages = kindergartenImageRepository.findByKindergartenId(kindergartenId);

        if (kindergartenImages == null || kindergartenImages.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_IMAGE_NOT_FOUND);
        }

        return kindergartenImages.stream()
                .map(KindergartenImage::getId)
                .collect(Collectors.toList());
    }
}
