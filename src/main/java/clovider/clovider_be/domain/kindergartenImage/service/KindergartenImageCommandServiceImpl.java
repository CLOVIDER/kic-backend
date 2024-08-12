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
@Transactional
@RequiredArgsConstructor
public class KindergartenImageCommandServiceImpl implements KindergartenImageCommandService {

    private final KindergartenImageRepository kindergartenImageRepository;

    @Override
    public List<Long> saveKindergartenImage(Kindergarten kindergarten, List<String> kindergardenUrls) {
        List<KindergartenImage> kindergartenImages = kindergardenUrls.stream()
                .map(url -> KindergartenImage.builder()
                        .image(url)
                        .kindergarten(kindergarten)
                        .build())
                .collect(Collectors.toList());

        List<KindergartenImage> savedImages = kindergartenImageRepository.saveAll(kindergartenImages);

        return savedImages.stream()
                .map(KindergartenImage::getId)
                .collect(Collectors.toList());
    }

    public List<Long> updateKindergartenImage(Kindergarten kindergarten, List<String> newImageUrls) {
        kindergartenImageRepository.deleteAllByKindergartenId(kindergarten.getId());

        List<KindergartenImage> newImages = newImageUrls.stream()
                .map(url -> KindergartenImage.builder()
                        .kindergarten(kindergarten)
                        .image(url)
                        .build())
                .collect(Collectors.toList());

        List<KindergartenImage> savedImages = kindergartenImageRepository.saveAll(newImages);

        return savedImages.stream()
                .map(KindergartenImage::getId)
                .collect(Collectors.toList());
    }
}
