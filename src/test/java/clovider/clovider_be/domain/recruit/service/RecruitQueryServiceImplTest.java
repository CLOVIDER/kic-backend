package clovider.clovider_be.domain.recruit.service;

import static org.assertj.core.api.Assertions.assertThat;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@Import(QuerydslConfig.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RecruitQueryServiceImplTest {

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    private KindergartenRepository kindergartenRepository;

    @Test
    @DisplayName("현재 진행 중인 모집 - 클래스반별 정렬")
    void getNowRecruitOrderByClass() {

        // given
        List<NowRecruit> recruits = recruitRepository.findNowRecruitOrderByClass(
                LocalDateTime.now());

        // when
        String kdg1 = recruits.get(0).getKindergartenNm();
        String kdg2 = recruits.get(5).getKindergartenNm();
        NowRecruit recruit = recruits.get(0);

        // then
        assertThat(recruits.size()).isEqualTo(6);
        assertThat(kdg1).isLessThan(kdg2);
        assertThat(recruit.getRecruitStartDt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(recruit.getSecondEndDt()).isAfterOrEqualTo(LocalDateTime.now());
    }


    @Test
    @DisplayName("Kindergarten ID로 모집 정보 조회하기")
    void getRecruitByKindergarten() {

        // given
        Kindergarten kindergarten = kindergartenRepository.findById(1L).get();

        // when
        List<Recruit> recruits = recruitRepository.findByKindergartenId(kindergarten.getId());
        Kindergarten kdg1 = recruits.get(0).getKindergarten();
        List<Integer> recruitCnts = recruits.stream()
                .map(Recruit::getRecruitCnt)
                .toList();
        AgeClass ageClass1 = recruits.get(0).getAgeClass();
        AgeClass ageClass2 = recruits.get(1).getAgeClass();

        // then
        assertThat(kdg1.getKindergartenNm()).isEqualTo("새빛");
        assertThat(recruits.size()).isEqualTo(3);
        assertThat(recruitCnts).allMatch(count -> count > 0,
                "All recruit counts should be greater than 0");
        assertThat(ageClass1).isNotEqualTo(ageClass2);
    }

    @Test
    @DisplayName("현재 모집기간 중인 모집 조회")
    void getNowRecruit() {

        // given
        List<Recruit> recruits = recruitRepository.findNowRecruit(LocalDateTime.now());

        // when
        LocalDateTime startDt = recruits.get(0).getRecruitStartDt();
        LocalDateTime endDt = recruits.get(0).getRecruitEndDt();

        // then
        assertThat(startDt).isBeforeOrEqualTo(endDt);
        assertThat(recruits.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Recruit ID로 모집 조회")
    void getRecruit() {

        // given
        Recruit recruit1 = recruitRepository.findById(1L).get();
        Recruit recruit2 = recruitRepository.findById(2L).get();

        // when
        Kindergarten kdg1 = recruit1.getKindergarten();
        Kindergarten kdg2 = recruit2.getKindergarten();
        AgeClass ageClass1 = recruit1.getAgeClass();
        AgeClass ageClass2 = recruit2.getAgeClass();

        // then
        assertThat(recruit1).isNotSameAs(recruit2);
        assertThat(kdg1).isNotSameAs(kdg2);
        assertThat(ageClass1).isEqualTo(ageClass2);
        assertThat(ageClass1.getDescription()).isEqualTo(AgeClass.TODDLER.getDescription());

    }

    @Test
    @DisplayName("RecruitInfo 조회")
    void getRecruitInfo() {

        // given
        Recruit recruit = recruitRepository.findRecruitInfoById(1L).get();

        // when
        RecruitInfo recruitInfo = LotteryResponse.toRecruitInfo(recruit);
        String kindergartenNm = recruitInfo.getKindergartenNm();
        String ageClass = recruitInfo.getAgeClass();

        // then
        assertThat(recruitInfo).isNotNull();
        assertThat(kindergartenNm).isEqualTo("새빛");
        assertThat(ageClass).isEqualTo(AgeClass.TODDLER.getDescription());
        assertThat(recruitInfo).isInstanceOf(RecruitInfo.class);
    }
}