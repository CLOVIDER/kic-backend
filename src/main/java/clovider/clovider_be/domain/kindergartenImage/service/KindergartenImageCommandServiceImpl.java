package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class KindergartenImageCommandServiceImpl implements KindergartenImageCommandService {

    private final KindergartenImageRepository kindergartenImageRepository;

    @Override
    public Long saveKindergartenImage(Kindergarten kindergarten, MultipartFile kindergartenImageFile) {
        //TODO: S3 버킷 저장 로직 필요
        String S3BucketUrl = "path/to/saved/image.jpg";

        KindergartenImage kindergartenImage = KindergartenImage.builder()
                .image(S3BucketUrl)
                .kindergarten(kindergarten)
                .build();

        kindergartenImage = kindergartenImageRepository.save(kindergartenImage);

        return kindergartenImage.getId();
    }
}
