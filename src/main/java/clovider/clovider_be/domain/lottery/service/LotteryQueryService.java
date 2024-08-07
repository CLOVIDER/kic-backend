package clovider.clovider_be.domain.lottery.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.admin.dto.AdminResponse.LotteryResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.ChildInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotteryQueryService {

    LotteryResultResponseDTO getLotteryResultByLotteryId(Long lotteryId);

    List<CompetitionRate> getRecruitRates(List<NowRecruit> recruits);

    Long getTotalApplication(List<Long> recruitIds);

    Long getUnAcceptApplication(List<Long> recruitIds);

    List<AcceptResult> getAcceptResult(List<Long> recruitIds);

    List<RecruitResult> getRecruitResult(Long recruitId);

    List<Long> getApplicationsByLotteries(List<Recruit> recruits);

    List<ChildInfo> getChildInfos(Long applicationId);

    Page<LotteryResult> getLotteryResultByLotteryId(Long kindergartenId, Pageable pageable, String value);
}
