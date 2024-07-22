package clovider.clovider_be.domain.kindergartenImage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class KindergartenImageCommandServiceImpl implements KindergartenImageCommandService {

    @Override
    public String saveKindergartenImage(MultipartFile kindergarten) {
        //TODO: S3 버킷 저장 로직 필요
        return "path/to/saved/image.jpg";
    }
}
