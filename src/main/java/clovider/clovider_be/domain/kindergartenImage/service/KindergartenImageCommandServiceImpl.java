package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KindergartenImageCommandServiceImpl implements KindergartenImageCommandService {

    private final KindergartenImageRepository kindergartenImageRepository;

    @Override
    public Long saveKindergartenImage(Kindergarten kindergarten, String kindergartenImageFile) {
        KindergartenImage kindergartenImage = KindergartenImage.builder()
                .image(kindergartenImageFile)
                .kindergarten(kindergarten)
                .build();

        kindergartenImage = kindergartenImageRepository.save(kindergartenImage);

        return kindergartenImage.getId();
    }
}
