package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergartenImage.KindergartenImage;
import clovider.clovider_be.domain.kindergartenImage.repository.KindergartenImageRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenImageQueryServiceImpl implements KindergartenImageQueryService {
    private final KindergartenImageRepository kindergartenImageRepository;

    @Override
    public String getKindergartenImageUrls(Long kindergartenId) {
        Optional<KindergartenImage> kindergartenImage = kindergartenImageRepository.findByKindergartenId(kindergartenId);

        if (kindergartenImage.isEmpty()) {
            return "default/image.png";
        }

        return kindergartenImage.get().getImage();
    }


    @Override
    public Optional<KindergartenImage> getKindergartenImage(Long kindergartenId) {
        return kindergartenImageRepository.findByKindergartenId(kindergartenId);
    }

    @Override
    public Long getKindergartenImageId(Long kindergartenId) {
        Optional<KindergartenImage> kindergartenImage = kindergartenImageRepository.findByKindergartenId(kindergartenId);

        if(kindergartenImage.isEmpty()){
            return 0L; // TODO: 디폴트 이미지 DB에 저장해두고, 해당 id값으로 바꾸기
        }
        return kindergartenImage.get().getId();
    }
}
