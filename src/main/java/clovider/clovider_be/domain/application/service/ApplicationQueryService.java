package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.dto.ApplicationResponse;

public interface ApplicationQueryService {
    ApplicationResponse applicationRead(Long Id);
    void applicationPagination();
}