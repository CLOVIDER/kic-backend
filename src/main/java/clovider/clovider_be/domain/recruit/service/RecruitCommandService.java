package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import java.util.List;

public interface RecruitCommandService {

    List<Long> resetKindergarten(Long kindergartenId);

}
