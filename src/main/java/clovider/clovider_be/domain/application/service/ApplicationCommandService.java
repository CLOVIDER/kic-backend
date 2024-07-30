package clovider.clovider_be.domain.application.service;


import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationCommandService {

    CustomResult applicationCreate(ApplicationRequest applicationRequest, Employee employee);
    CustomResult applicationUpdate(Long Id, ApplicationRequest applicationRequest);
    CustomResult applicationDelete(Long Id);
    CustomResult applicationTempSave(ApplicationRequest ApplicationRequest, Employee employee);
}
