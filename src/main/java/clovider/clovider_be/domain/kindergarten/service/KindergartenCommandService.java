package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;

public interface KindergartenCommandService {

    CustomResult registerKdg(String kdgName, String kdgAddress, String kdgScale, String kdgNo, String kdgTime, String kdgInfo, String kdgImage);
    CustomResult deleteKdg(Long kdgId);

}
