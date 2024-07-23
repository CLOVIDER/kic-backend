package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KindergartenImageCommandServiceImpl implements KindergartenImageCommandService {

    private final KindergartenImageRepository kindergartenImageRepository;
    private final KindergartenImageQueryService kindergartenImageQueryService;

    @Override
    public Long saveKindergartenImage(Kindergarten kindergarten, String kindergartenImageFile) {
        KindergartenImage kindergartenImage = KindergartenImage.builder()
                .image(kindergartenImageFile)
                .kindergarten(kindergarten)
                .build();

        kindergartenImage = kindergartenImageRepository.save(kindergartenImage);

        return kindergartenImage.getId();
    }

    public Long updateKindergartenImage(Kindergarten kindergarten, String newImageUrl) {

        List<KindergartenImage> existingImages = kindergartenImageQueryService.getKindergartenImage(kindergarten.getId());
        kindergartenImageRepository.deleteAll(existingImages);

        // 새로운 이미지 추가
        KindergartenImage newImage = KindergartenImage.builder()
                .image(newImageUrl)
                .kindergarten(kindergarten)
                .build();
        newImage = kindergartenImageRepository.save(newImage);

        return newImage.getId();
    }
}
