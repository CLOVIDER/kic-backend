package clovider.clovider_be.domain.lottery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.domain.utils.CreateUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LotteryQueryServiceImplTest {

    @Mock
    private LotteryRepository lotteryRepository;

    @InjectMocks
    private LotteryQueryServiceImpl lotteryQueryService;

    @Test
    @DisplayName("진행 중인 모집의 경쟁률 조회")
    void getRecruitRates() {

        // given
        List<NowRecruit> nowRecruits = CreateUtil.getNowRecruitList();
        List<Long> recruitIds = nowRecruits.stream().map(NowRecruit::getId).toList();
        List<CompetitionRate> competitionRates = new ArrayList<>();
        CompetitionRate competitionRate = new CompetitionRate(1L, 0.4);
        competitionRates.add(competitionRate);

        // when
        when(lotteryRepository.findCompetitionRates(recruitIds)).thenReturn(competitionRates);
        List<CompetitionRate> recruitRates = lotteryQueryService.getRecruitRates(nowRecruits);
        Long recruitId1 = recruitRates.get(0).getRecruitId();
        Double competitionRate1 = recruitRates.get(0).getCompetitionRate();

        // then
        verify(lotteryRepository, times(1)).findCompetitionRates(recruitIds);
        assertThat(recruitId1).isEqualTo(1L);
        assertThat(recruitRates.size()).isEqualTo(1);
        assertThat(competitionRate1).matches(c -> c > 0);
        assertThat(recruitRates.get(0)).isInstanceOf(CompetitionRate.class);
    }

    @Test
    @DisplayName("총 신청자 수 조회")
    void getTotalApplication() throws NoSuchFieldException, IllegalAccessException {

        // given
        LocalDateTime now = LocalDateTime.of(2024, 8, 8, 12, 0);
        List<Recruit> recruitList = CreateUtil.getRecruitList();
        List<Lottery> lotteryList = CreateUtil.getLotteryList();
        List<Long> recruitIds = recruitList.stream().map(Recruit::getId).toList();

        // when
        when(lotteryRepository.findTotalApplication(recruitIds)).thenReturn(
                (long) lotteryList.size());

        Long totalApplication = lotteryQueryService.getTotalApplication(recruitIds);

        // then
        assertThat(totalApplication).isEqualTo(8);
    }

    @Test
    @DisplayName("승인 대기 수 조회")
    void getUnAcceptApplication() {

        // given
        List<Recruit> recruitList = CreateUtil.getRecruitList();
        List<Long> recruitIds = recruitList.stream().map(Recruit::getId).toList();
        when(lotteryRepository.findUnAcceptApplication(recruitIds)).thenReturn((long) 8);

        // when
        Long unAcceptApplication = lotteryQueryService.getUnAcceptApplication(recruitIds);

        // then
        verify(lotteryRepository, times(1)).findUnAcceptApplication(recruitIds);
        assertThat(unAcceptApplication).isEqualTo(8);
    }

    @Test
    @DisplayName("어린이집별 신청현황")
    void getAcceptResult() {

        // given
        LocalDateTime now = LocalDateTime.of(2024, 8, 8, 12, 0);
        List<Recruit> recruitList = CreateUtil.getRecruitList();
        List<Long> recruitIds = recruitList.stream().map(Recruit::getId).toList();
        List<AcceptResult> acceptResultList = new ArrayList<>();
        AcceptResult acceptResult = AcceptResult.builder()
                .kindergartenNm("애플 어린이집")
                .acceptCnt(0)
                .unAcceptCnt(8)
                .waitCnt(0)
                .build();
        acceptResultList.add(acceptResult);

        // when
        when(lotteryRepository.findAcceptStatus(recruitIds)).thenReturn(acceptResultList);
        List<AcceptResult> acceptResults = lotteryQueryService.getAcceptResult(recruitIds);

        String kindergartenNm1 = acceptResults.get(0).getKindergartenNm();
        Integer acceptCnt = acceptResults.get(0).getAcceptCnt();
        Integer unAcceptCnt = acceptResults.get(0).getUnAcceptCnt();
        Integer waitCnt = acceptResults.get(0).getWaitCnt();

        // then
        assertThat(acceptResultList.size()).isEqualTo(1);
        assertThat(kindergartenNm1).isEqualTo("애플 어린이집");
        assertThat(acceptCnt).isEqualTo(0);
        assertThat(unAcceptCnt).isEqualTo(8);
        assertThat(waitCnt).isEqualTo(0);

    }
}