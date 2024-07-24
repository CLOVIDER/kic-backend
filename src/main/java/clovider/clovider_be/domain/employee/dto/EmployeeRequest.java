package clovider.clovider_be.domain.employee.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;

public class EmployeeRequest {

    @Getter
    public static class ChangePassword {

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }

    public static Employee toEntity(String accountId, String password) {

        return Employee.builder()
                .nameKo("EMPLOYEE_1")
                .accountId(accountId)
                .password(password)
                .employeeNo("12345678")
                .joinDt(LocalDate.of(2024, 7, 1))
                .dept("IT")
                .role(Role.EMPLOYEE)
                .build();
    }
}
