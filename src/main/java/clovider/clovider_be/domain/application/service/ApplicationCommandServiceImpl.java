package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.entity.Application;
import clovider.clovider_be.domain.application.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ApplicationCommandServiceImpl implements ApplicationCommandService {

    public ApplicationCommandServiceImpl(ApplicationRepository applicationRepository) {
    }

    @Override
    public void applicationCreate(Application application) {}

    @Override
    public void applicationUpdate(Application application) {}

    @Override
    public void applicationDelete(Long Id) {}
}
