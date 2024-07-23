package clovider.clovider_be.domain.application.service;


import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationCommandService {

    CustomResult applicationCreate(ApplicationRequest applicationRequest);
    CustomResult applicationUpdate(Long Id, ApplicationRequest applicationRequest);
    CustomResult applicationDelete(Long Id);
    void applicationTempSave(Application application);
}
