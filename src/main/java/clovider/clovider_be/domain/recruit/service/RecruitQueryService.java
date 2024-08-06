package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import java.util.List;

public interface RecruitQueryService {

    List<Recruit> getRecruitByKindergarten(Long kindergartenId);

    NowRecruits getNowRecruitOrderByClass();

    List<Long> getRecruitIngAndScheduled();

    List<Recruit> getNowRecruit();

    RecruitInfo getRecruitInfo(Long recruitId);

    Recruit getRecruit(Long id);

    List<Recruit> getRecruitAndKindergarten();
}
