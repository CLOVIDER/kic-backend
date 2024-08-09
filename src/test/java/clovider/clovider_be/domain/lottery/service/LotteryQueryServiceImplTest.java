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

    @Mock
    private RecruitQueryService recruitQueryService;

    @InjectMocks
    private LotteryQueryServiceImpl lotteryQueryService;

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

    private List<Lottery> getLotteryList() {

        List<Recruit> recruitList = getRecruitList();
        List<Application> applicationList = getApplicationList();
        String[] names = {"준희아이", "민수아이", "영희아이", "철수아이"};
        List<Lottery> lotteries = new ArrayList<>();

        for (int i = 0; i < recruitList.size(); i++) {
            for (int j = 0; j < applicationList.size(); j++) {
                Lottery lottery = Lottery.builder()
                        .recruit(recruitList.get(i))
                        .application(applicationList.get(j))
                        .rankNo(0)
                        .result(Result.WAIT)
                        .isRegistry('0')
                        .childNm(names[j])
                        .build();
                lotteries.add(lottery);
            }
        }
        return lotteries;
    }

    private List<Application> getApplicationList() {

        List<Employee> employeeList = getEmployeeList();
        List<Application> applications = new ArrayList<>();

        for (Employee employee : employeeList) {
            Application application = Application.builder()
                    .workYears(3)
                    .childrenCnt(1)
                    .isDisability('0')
                    .isDualIncome('0')
                    .isEmployeeCouple('1')
                    .isSibling('0')
                    .isTemp(Save.APPLIED)
                    .isAccept(Accept.WAIT)
                    .employee(employee)
                    .build();
            applications.add(application);
        }
        return applications;
    }

    private List<Employee> getEmployeeList() {

        List<Employee> employees = new ArrayList<>();

        String[] names = {"준희", "민수", "영희", "철수"};
        String[] accountIds = {"qwer123", "qwer124", "qwer125", "qwer126"};
        String[] employeeNos = {"20292929", "20292930", "20292931", "20292932"};

        for (int i = 0; i < names.length; i++) {
            Employee employee = Employee.builder()
                    .nameKo(names[i])
                    .accountId(accountIds[i])
                    .password("1234")
                    .employeeNo(employeeNos[i])
                    .joinDt(LocalDate.of(2020, 2, 2))
                    .dept("IT")
                    .role(Role.EMPLOYEE)
                    .build();
            employees.add(employee);
        }
        return employees;
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
    @DisplayName("진행 중인 모집의 경쟁률 조회")
    void getRecruitRates() {

        // given
        List<NowRecruit> nowRecruits = getNowRecruitList();
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
    void getTotalApplication() {

        // given
        LocalDateTime now = LocalDateTime.of(2024, 8, 8, 12, 0);
        List<Recruit> recruitList = getRecruitList();
        List<Lottery> lotteryList = getLotteryList();
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
        List<Recruit> recruitList = getRecruitList();
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
        List<Recruit> recruitList = getRecruitList();
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