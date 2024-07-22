package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationReadDto;

public interface ApplicationQueryService {
    //    ApplicationReadDto applicationRead(Long Id);
    void applicationPagination();
}