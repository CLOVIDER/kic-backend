package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import org.springframework.web.multipart.MultipartFile;

public interface KindergartenCommandService {

    KindergartenResponse registerKdg(KindergartenRequest kindergartenRequest);
    CustomResult deleteKdg(Long kdgId);

}
