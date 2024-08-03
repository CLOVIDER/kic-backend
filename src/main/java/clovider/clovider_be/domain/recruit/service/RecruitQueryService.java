package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import java.util.List;

public interface RecruitQueryService {

    List<Recruit> getRecruitByKindergarten(Long kindergartenId);

    List<Recruit> getNowRecruitOrderByClass();

    List<Recruit> getNowRecruit();

    RecruitInfo getRecruitInfo(Long recruitId);

    Recruit getRecruit(Long id);

    List<Recruit> getRecruitAndKindergarten();
}
