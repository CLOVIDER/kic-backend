package clovider.clovider_be.domain.utils;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.qna.Qna;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestUtils {
    public static Employee createEmployee(Long id, String nameKo, String accountId, String password, String employeeNo, LocalDate joinDt, String dept, Role role) {
        return Employee.builder()
                .id(id)
                .nameKo(nameKo)
                .accountId(accountId)
                .password(password)
                .employeeNo(employeeNo)
                .joinDt(joinDt)
                .dept(dept)
                .role(role)
                .build();
    }

    public static Qna createQna(Long id, Employee employee) throws NoSuchFieldException, IllegalAccessException {
        Qna qna = Qna.builder()
                .id(id)
                .title("문제입니다")
                .answer(null)
                .question("문제가 있어요")
                .employee(employee)
                .isVisibility('1')
                .build();

        setTimeField(qna, "createdAt", LocalDateTime.now().minusDays(2));
        setTimeField(qna, "updatedAt", LocalDateTime.now());

        return qna;
    }

    public static void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void setTimeField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> superclass = obj.getClass().getSuperclass();
        Field field = superclass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
