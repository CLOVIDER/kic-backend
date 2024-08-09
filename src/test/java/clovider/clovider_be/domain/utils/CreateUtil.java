package clovider.clovider_be.domain.utils;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.Save;
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
