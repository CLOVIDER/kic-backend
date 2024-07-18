package clovider.clovider_be.domain.application.service;


import clovider.clovider_be.domain.application.Application;

public interface ApplicationCommandService {

    void applicationCreate(Application application);
    void applicationUpdate(Application application);
    void applicationDelete(Long Id);
}
