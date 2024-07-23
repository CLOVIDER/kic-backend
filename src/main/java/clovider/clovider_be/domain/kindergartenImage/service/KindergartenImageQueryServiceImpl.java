package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.List;
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
    public List<String> getKindergartenImageUrls(Long KindergartenId) {
        List<KindergartenImage> kindergartenImages = kindergartenImageRepository.findByKindergartenId(KindergartenId);
        List<String> imageUrls = kindergartenImages.stream()
                .map(KindergartenImage::getImage)
                .collect(Collectors.toList());

        return imageUrls;
    }

    @Override
    public List<KindergartenImage> getKindergartenImage(Long kindergartenId) {
        return kindergartenImageRepository.findByKindergartenId(kindergartenId);
    }
}
