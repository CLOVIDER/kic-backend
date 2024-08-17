package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitCreationInfo;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecruitQueryService {

    List<Recruit> getRecruitByKindergarten(Long kindergartenId);

    NowRecruits getNowRecruitOrderByClass(LocalDateTime now);

    List<Long> getRecruitIngAndScheduled(LocalDateTime now);

    List<Recruit> getNowRecruit(LocalDateTime now);

    RecruitInfo getRecruitInfo(Long recruitId);

    Recruit getRecruit(Long id);

    List<Recruit> getRecruitAndKindergarten();

    List<Recruit> getRecruitResultWithRecruitAndKindergarten();

    RecruitCreationInfo getRecruitCreationInfo();

    Optional<Recruit> getRecruitByKindergarten(Kindergarten kindergarten, Integer ageClass, LocalDateTime now);
}
