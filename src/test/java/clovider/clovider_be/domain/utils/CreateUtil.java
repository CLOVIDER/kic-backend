package clovider.clovider_be.domain.utils;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.Save;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.NowRecruit;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CreateUtil {

    public static List<Employee> getEmployeeList()
            throws NoSuchFieldException, IllegalAccessException {

        List<Employee> employees = new ArrayList<>();

        String[] names = {"준희", "민수", "영희", "철수"};
        String[] accountIds = {"qwer123", "qwer124", "qwer125", "qwer126"};
        String[] employeeNos = {"20292929", "20292930", "20292931", "20292932"};

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        for (int i = 0; i < names.length; i++) {

            Employee employee = Employee.builder()
                    .id((long) i + 1)
                    .nameKo(names[i])
                    .accountId(accountIds[i])
                    .password(encoder.encode("1234"))
                    .employeeNo(employeeNos[i])
                    .joinDt(LocalDate.of(2020, 2, 2))
                    .dept("IT")
                    .role(Role.EMPLOYEE)
                    .build();
            setField(employee, "createdAt", LocalDateTime.of(2024, 8, 1, 12, 0));
            setField(employee, "updatedAt", LocalDateTime.now());
            employees.add(employee);
        }
        return employees;
    }

    private static void setField(Object obj, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Class<?> superclass = obj.getClass().getSuperclass();
        Field field = superclass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static List<Kindergarten> getKindergartenList() {
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

    public static List<Recruit> getRecruitList() {

        List<Recruit> recruits = new ArrayList<>();
        List<Kindergarten> kindergartens = getKindergartenList();

        Recruit recruit1 = Recruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .recruitCnt(20)
                .ageClass(1)
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
                .ageClass(2)
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

    public static List<Lottery> getLotteryList() throws NoSuchFieldException, IllegalAccessException {

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

    public static List<NowRecruit> getNowRecruitList() {

        List<NowRecruit> recruits = new ArrayList<>();
        List<Kindergarten> kindergartens = getKindergartenList();

        NowRecruit nowRecruit1 = NowRecruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .ageClass(1)
                .firstStartDt(LocalDateTime.of(2024, 9, 2, 12, 0))
                .firstEndDt(LocalDateTime.of(2024, 9, 10, 12, 0))
                .secondStartDt(LocalDateTime.of(2024, 9, 12, 12, 0))
                .secondEndDt(LocalDateTime.of(2024, 9, 20, 12, 0))
                .kindergartenNm(kindergartens.get(0).getKindergartenNm())
                .build();

        NowRecruit nowRecruit2 = NowRecruit.builder()
                .recruitStartDt(LocalDateTime.of(2024, 7, 1, 12, 0))
                .recruitEndDt(LocalDateTime.of(2024, 9, 1, 12, 0))
                .ageClass(2)
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


    public static List<Application> getApplicationList()
            throws NoSuchFieldException, IllegalAccessException {

        List<Employee> employeeList = getEmployeeList();
        List<Application> applications = new ArrayList<>();

        for (Employee employee : employeeList) {
            long i = 1;
            Application application = Application.builder()
                    .id(i++)
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

}
