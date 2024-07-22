package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import org.springframework.web.multipart.MultipartFile;

public interface KindergartenCommandService {

    KindergartenResponse registerKdg(String kdgName, String kdgAddress, String kdgScale, String kdgNo, String kdgTime, String kdgInfo, MultipartFile kdgImage);
    CustomResult deleteKdg(Long kdgId);

}
