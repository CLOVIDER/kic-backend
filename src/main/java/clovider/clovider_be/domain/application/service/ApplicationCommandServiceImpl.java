package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.application.dto.ApplicationWriteDto;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import java.net.Authenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationCommandServiceImpl implements ApplicationCommandService {

    private final ApplicationRepository applicationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void applicationCreate(ApplicationWriteDto applicationWriteDto) { //후에 토큰값 인자로 추가
//        Integer employeeId = jwtUtil.extractEmployeeId(token);
//          Employee employee = employeeRepository.findById(employeeId)
//                  .orElseThrow();
//        Application application = applicationWriteDto.toEntity(employee);
//        applicationRepository.save(application);
    }

    @Override
    public void applicationUpdate(Long Id, ApplicationUpdateDto applicationUpdateDto) {
        Application application = applicationRepository.findById(Id).orElseThrow();
        application.update(applicationUpdateDto);

    }

    @Override
    public void applicationDelete(Long Id) {
        Application application = applicationRepository.findById(Id).orElseThrow();
        applicationRepository.delete(application);
    }

    @Override
    public void applicationTempSave(Application application) {

    }


}