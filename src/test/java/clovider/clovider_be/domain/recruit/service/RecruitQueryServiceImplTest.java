package clovider.clovider_be.domain.recruit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.RecruitInfo;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruits;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecruitQueryServiceImplTest {

    @Mock
    private RecruitRepository recruitRepository;

    @InjectMocks
    private RecruitQueryServiceImpl recruitQueryService;

    private List<Kindergarten> getKindergartenList() {
        List<Kindergarten> kindergartens = new ArrayList<>();
        Kindergarten kindergarten1 = Kindergarten.builder()
                .kindergartenNm("애플 어린이집")
                .kindergartenAddr("실리콘벨리")
                .kindergartenScale(100)
                .kindergartenCapacity(300)
                .kindergartenNo("02-123-4567")
                .kindergartenTime("24시간")
                .kindergartenInfo("어린이집 설명")
                .build();
        Kindergarten kindergarten2 = Kindergarten.builder()
                .kindergartenNm("구글 어린이집")
                .kindergartenAddr("실리콘벨리")
                .kindergartenScale(200)
                .kindergartenCapacity(400)
                .kindergartenNo("02-999-8888")
                .kindergartenTime("24시간")
                .kindergartenInfo("어린이집 설명")
                .build();
        kindergartens.add(kindergarten1);
        kindergartens.add(kindergarten2);
        return kindergartens;
    }

    private List<Recruit> getRecruitList() {

        List<Recruit> recruits = new ArrayList<>();
        List<Kindergarten> kindergartens = getKindergartenList();

        Recruit recruit1 = Recruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .recruitCnt(20)
                .ageClass(AgeClass.KID)
                .firstStartDt(LocalDateTime.of(2024, 9, 2, 12, 0))
                .firstEndDt(LocalDateTime.of(2024, 9, 10, 12, 0))
                .secondStartDt(LocalDateTime.of(2024, 9, 12, 12, 0))
                .secondEndDt(LocalDateTime.of(2024, 9, 20, 12, 0))
                .kindergarten(kindergartens.get(0))
                .workYearsUsage('1')
                .isSingleParentUsage('1')
                .childrenCntUsage('1')
                .isDisabilityUsage('1')
                .isDualIncomeUsage('1')
                .isEmployeeCoupleUsage('1')
                .isSiblingUsage('0')
                .build();
        Recruit recruit2 = Recruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .recruitCnt(20)
                .ageClass(AgeClass.TODDLER)
                .firstStartDt(LocalDateTime.of(2024, 9, 2, 12, 0))
                .firstEndDt(LocalDateTime.of(2024, 9, 10, 12, 0))
                .secondStartDt(LocalDateTime.of(2024, 9, 12, 12, 0))
                .secondEndDt(LocalDateTime.of(2024, 9, 20, 12, 0))
                .kindergarten(kindergartens.get(0))
                .workYearsUsage('1')
                .isSingleParentUsage('1')
                .childrenCntUsage('1')
                .isDisabilityUsage('1')
                .isDualIncomeUsage('1')
                .isEmployeeCoupleUsage('1')
                .isSiblingUsage('0')
                .build();

        recruits.add(recruit1);
        recruits.add(recruit2);
        return recruits;
    }

    private List<NowRecruit> getNowRecruitList() {

        List<NowRecruit> recruits = new ArrayList<>();
        List<Kindergarten> kindergartens = getKindergartenList();

        NowRecruit nowRecruit1 = NowRecruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .ageClass(AgeClass.KID.getDescription())
                .firstStartDt(LocalDateTime.of(2024, 9, 2, 12, 0))
                .firstEndDt(LocalDateTime.of(2024, 9, 10, 12, 0))
                .secondStartDt(LocalDateTime.of(2024, 9, 12, 12, 0))
                .secondEndDt(LocalDateTime.of(2024, 9, 20, 12, 0))
                .kindergartenNm(kindergartens.get(0).getKindergartenNm())
                .build();

        NowRecruit nowRecruit2 = NowRecruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .ageClass(AgeClass.TODDLER.getDescription())
                .firstStartDt(LocalDateTime.of(2024, 9, 2, 12, 0))
                .firstEndDt(LocalDateTime.of(2024, 9, 10, 12, 0))
                .secondStartDt(LocalDateTime.of(2024, 9, 12, 12, 0))
                .secondEndDt(LocalDateTime.of(2024, 9, 20, 12, 0))
                .kindergartenNm(kindergartens.get(1).getKindergartenNm())
                .build();

        recruits.add(nowRecruit1);
        recruits.add(nowRecruit2);
        return recruits;
    }


    @Test
    @DisplayName("현재 진행 중인 모집 - 클래스반별 정렬")
    void getNowRecruitOrderByClass() {

        // given
        List<NowRecruit> nowRecruitList = getNowRecruitList();
        LocalDateTime now = LocalDateTime.of(2024, 8, 8, 12, 0);

        // when
        when(recruitRepository.findNowRecruitOrderByClass(now)).thenReturn(nowRecruitList);
        NowRecruits nowRecruits = recruitQueryService.getNowRecruitOrderByClass(now);

        List<NowRecruit> recruits = nowRecruits.getNowRecruits();
        NowRecruit recruit = recruits.get(0);

        // then
        assertThat(recruits.size()).isEqualTo(2);
        assertThat(recruit.getRecruitStartDt()).isBeforeOrEqualTo(now);
        assertThat(recruit.getSecondEndDt()).isAfterOrEqualTo(now);
    }


    @Test
    @DisplayName("Kindergarten ID로 모집 정보 조회하기")
    void getRecruitByKindergarten() {

        // given
        List<Kindergarten> kindergartenList = getKindergartenList();
        Kindergarten kindergarten = kindergartenList.get(0);
        List<Recruit> recruitList = getRecruitList();
        Mockito.when(recruitRepository.findByKindergartenId(kindergarten.getId()))
                .thenReturn(recruitList);

        // when
        List<Recruit> recruits = recruitQueryService.getRecruitByKindergarten(kindergarten.getId());
        Kindergarten kdg1 = recruits.get(0).getKindergarten();
        List<Integer> recruitCnts = recruits.stream()
                .map(Recruit::getRecruitCnt)
                .toList();

        // then
        assertThat(kdg1.getKindergartenNm()).isEqualTo("애플 어린이집");
        assertThat(recruits.size()).isEqualTo(2);
        assertThat(recruitCnts).allMatch(count -> count > 0,
                "All recruit counts should be greater than 0");
    }

    @Test
    @DisplayName("현재 모집기간 중인 모집 조회")
    void getNowRecruit() {

        // given
        LocalDateTime now = LocalDateTime.of(2024, 8, 8, 12, 0);
        List<Recruit> recruitList = getRecruitList();
        when(recruitRepository.findNowRecruit(now)).thenReturn(recruitList);

        // when
        List<Recruit> recruits = recruitQueryService.getNowRecruit(now);

        LocalDateTime startDt = recruits.get(0).getRecruitStartDt();
        LocalDateTime endDt = recruits.get(0).getRecruitEndDt();

        // then
        assertThat(startDt).isBeforeOrEqualTo(endDt);
        assertThat(recruits.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Recruit ID로 모집 조회")
    void getRecruit() {

        // given
        List<Recruit> recruitList = getRecruitList();
        Recruit recruit1 = recruitList.get(0);
        Recruit recruit2 = recruitList.get(1);

        // when
        when(recruitRepository.findById(recruit1.getId())).thenReturn(Optional.of(recruit1));
        Recruit recruit = recruitQueryService.getRecruit(recruit1.getId());
        Kindergarten kdg1 = recruit1.getKindergarten();
        Kindergarten kdg2 = recruit2.getKindergarten();
        AgeClass ageClass1 = recruit1.getAgeClass();

        // then
        verify(recruitRepository, times(1)).findById(recruit1.getId());
        assertThat(recruit).isEqualTo(recruit1);
        assertThat(recruit1).isNotSameAs(recruit2);
        assertThat(kdg1).isSameAs(kdg2);
        assertThat(ageClass1.getDescription()).isEqualTo(AgeClass.KID.getDescription());

    }

    @Test
    @DisplayName("RecruitInfo 조회")
    void getRecruitInfo() {

        // given
        List<Recruit> recruitList = getRecruitList();
        Recruit recruit1 = recruitList.get(0);

        // when
        when(recruitRepository.findRecruitInfoById(recruit1.getId())).thenReturn(
                Optional.of(recruit1));

        RecruitInfo recruitInfo = recruitQueryService.getRecruitInfo(recruit1.getId());
        String kindergartenNm = recruitInfo.getKindergartenNm();
        String ageClass = recruitInfo.getAgeClass();

        // then
        assertThat(recruitInfo).isNotNull();
        assertThat(kindergartenNm).isEqualTo("애플 어린이집");
        assertThat(ageClass).isNotEqualTo(AgeClass.TODDLER.getDescription());
        assertThat(recruitInfo).isInstanceOf(RecruitInfo.class);
    }
}