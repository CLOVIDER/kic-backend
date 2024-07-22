package clovider.clovider_be.domain.kindergartenImage.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import org.springframework.web.multipart.MultipartFile;

public interface KindergartenImageCommandService {
    Long saveKindergartenImage(Kindergarten kindergarten, MultipartFile kindergartenImage);

}
