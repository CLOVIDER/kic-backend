package clovider.clovider_be.domain.lottery.service;

import static org.assertj.core.api.Assertions.assertThat;

import clovider.clovider_be.domain.admin.dto.AdminResponse.AcceptResult;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.CompetitionRate;
import clovider.clovider_be.domain.lottery.repository.LotteryRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
import clovider.clovider_be.domain.recruit.service.RecruitQueryServiceImpl;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class LotteryQueryServiceImplTest {

    @Autowired
    private RecruitQueryService recruitQueryService;

    @Autowired
    private LotteryRepository lotteryRepository;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RecruitQueryService recruitQueryService(RecruitRepository recruitRepository) {
            return new RecruitQueryServiceImpl(recruitRepository);
        }
    }

    @Test
    @DisplayName("진행 중인 모집의 경쟁률 조회")
    void getRecruitResult() {

        // given
        NowRecruits recruits = recruitQueryService.getNowRecruitOrderByClass();
        List<Long> recruitIds = recruits.getNowRecruits().stream()
                .map(NowRecruit::getId)
                .toList();

        // when
        List<CompetitionRate> competitionRates = lotteryRepository.findCompetitionRates(recruitIds);

        List<Double> rates = competitionRates.stream()
                .map(CompetitionRate::getCompetitionRate)
                .toList();

        Long recruitId1 = competitionRates.get(0).getRecruitId();
        Long recruitId2 = competitionRates.get(1).getRecruitId();

        // then
        assertThat(competitionRates.size()).isEqualTo(6);
        assertThat(rates).allMatch(rate -> rate instanceof Double,
                "All rates should be of type Double");
        assertThat(recruitId1).isLessThan(recruitId2);
        assertThat(competitionRates.get(0)).isInstanceOf(CompetitionRate.class);
    }

    @Test
    @DisplayName("총 신청자 수 조회")
    void getTotalApplication() {

        // given
        List<Long> recruitIds = recruitQueryService.getRecruitIngAndScheduled();

        // when
        Long totalApplication = lotteryRepository.findTotalApplication(recruitIds);

        // then
        assertThat(totalApplication).isEqualTo(16);
    }

    @Test
    @DisplayName("승인 대기 수 조회")
    void getUnAcceptApplication() {

        // given
        List<Long> recruitIds = recruitQueryService.getRecruitIngAndScheduled();

        // when
        Long unAcceptApplication = lotteryRepository.findUnAcceptApplication(recruitIds);

        // then
        assertThat(unAcceptApplication).isEqualTo(0);
    }

    @Test
    @DisplayName("어린이집별 신청현황")
    void getAcceptResult() {

        // given
        List<Long> recruitIds = recruitQueryService.getRecruitIngAndScheduled();

        // when
        List<AcceptResult> acceptStatus = lotteryRepository.findAcceptStatus(recruitIds);
        String kindergartenNm1 = acceptStatus.get(0).getKindergartenNm();
        String kindergartenNm2 = acceptStatus.get(1).getKindergartenNm();
        Integer acceptCnt = acceptStatus.get(0).getAcceptCnt();
        Integer unAcceptCnt = acceptStatus.get(0).getUnAcceptCnt();
        Integer waitCnt = acceptStatus.get(0).getWaitCnt();

        // then
        assertThat(acceptStatus.size()).isEqualTo(2);
        assertThat(kindergartenNm1).isEqualTo("우유");
        assertThat(kindergartenNm2).isEqualTo("새빛");
        assertThat(acceptCnt).isEqualTo(8);
        assertThat(unAcceptCnt).isEqualTo(0);
        assertThat(waitCnt).isEqualTo(0);

    }
}