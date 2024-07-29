package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationReadDto;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.lottery.Lottery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public ApplicationReadDto applicationRead(Long Id){
        Application savedApplication = applicationRepository.findById(Id).orElseThrow();

        return ApplicationReadDto.toEntity(savedApplication);
    }

    @Override
    public void applicationPagination()
    {}

    @Override
    public List<Application> getNowApplications(List<Lottery> lotteries) {
        return lotteries.stream()
                .map(Lottery::getApplication)
                .toList();
    }
}