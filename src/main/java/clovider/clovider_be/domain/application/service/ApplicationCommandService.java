package clovider.clovider_be.domain.application.service;


import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.application.dto.ApplicationWriteDto;
import clovider.clovider_be.domain.common.CustomResult;

public interface ApplicationCommandService {

    CustomResult applicationCreate(ApplicationWriteDto applicationWriteDto);
    void applicationUpdate(Long Id, ApplicationUpdateDto applicationUpdateDto);
    void applicationDelete(Long Id);
    void applicationTempSave(Application application);
}
