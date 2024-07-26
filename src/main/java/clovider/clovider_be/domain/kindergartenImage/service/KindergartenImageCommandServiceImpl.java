package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.List;
import java.util.Optional;
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
        Optional<KindergartenImage> existingImage = kindergartenImageQueryService.getKindergartenImage(kindergarten.getId());

        if (existingImage.isPresent()) {
            kindergartenImageRepository.delete(existingImage.get());
        }

        KindergartenImage newImage = KindergartenImage.builder()
                .image(newImageUrl)
                .kindergarten(kindergarten)
                .build();
        newImage = kindergartenImageRepository.save(newImage);

        return newImage.getId();
    }
}
