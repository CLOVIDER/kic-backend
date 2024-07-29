package clovider.clovider_be.domain.application.service;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.application.dto.ApplicationReadDto;
import clovider.clovider_be.domain.lottery.Lottery;
import java.util.List;

public interface ApplicationQueryService {

    ApplicationReadDto applicationRead(Long Id);

    void applicationPagination();

    List<Application> getNowApplications(List<Lottery> lotteries);
}