package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    public ApplicationQueryServiceImpl(ApplicationRepository applicationRepository) {
    }

    @Override
    public void applicationRead(Long Id){
    };
}
