package clovider.clovider_be.domain.kindergartenImage.service;

import org.springframework.web.multipart.MultipartFile;

public interface KindergartenImageCommandService {
    String saveKindergartenImage(MultipartFile kindergartenImage);

}
