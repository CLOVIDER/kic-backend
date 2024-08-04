package clovider.clovider_be.domain.employee.dto;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.global.util.TimeUtil;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmployeeResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeInfo {

        private String nameKo;
        private String accountId;
        private String employeeNo;
        private Boolean isCouple;
        private String workedAt;
    }

    public static EmployeeInfo toEmployeeInfo(Employee employee) {

        return EmployeeInfo.builder()
                .nameKo(employee.getNameKo())
                .accountId(employee.getAccountId())
                .employeeNo(employee.getEmployeeNo())
                .isCouple(employee.getCoupleNo() == null)
                .workedAt(TimeUtil.formattedDate(employee.getCreatedAt()))
                .build();

    }
}
