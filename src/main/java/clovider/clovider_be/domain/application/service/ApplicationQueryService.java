package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.common.CustomPage;

public interface ApplicationQueryService {
    ApplicationResponse applicationRead(Long Id);
    CustomPage<ApplicationResponse> applicationListRead(int page, int size);
}