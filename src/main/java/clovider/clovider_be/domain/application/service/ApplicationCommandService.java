package clovider.clovider_be.domain.application.service;


import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.application.dto.ApplicationWriteDto;
import clovider.clovider_be.domain.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationCommandService {

    CustomResult applicationCreate(ApplicationWriteDto applicationWriteDto);
    CustomResult applicationUpdate(Long Id, ApplicationUpdateDto applicationUpdateDto);
    CustomResult applicationDelete(Long Id);
    void applicationTempSave(Application application);
}
