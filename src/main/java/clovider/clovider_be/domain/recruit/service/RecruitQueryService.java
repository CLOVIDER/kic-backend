package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;

public interface RecruitQueryService {

    List<Recruit> getRecruitByKindergarten(Long kindergartenId);

}
