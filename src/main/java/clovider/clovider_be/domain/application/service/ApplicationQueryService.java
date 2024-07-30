package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationResponse;
import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.employee.Employee;

public interface ApplicationQueryService {
    ApplicationResponse applicationRead(Employee employee);
    ApplicationResponse applicationIdRead(Long Id);
    CustomPage<ApplicationResponse> applicationListRead(int page, int size);

    Application getApplication(Long Id);
}
