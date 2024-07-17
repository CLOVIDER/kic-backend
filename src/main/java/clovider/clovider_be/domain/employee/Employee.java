package clovider.clovider_be.domain.employee;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Employee extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "employee_id")
    private Long id;

    private String nameKo;

    private String accountId;

    private String password;

    private String employeeNo;

    private LocalDate joinDate;

    private String dept;

    private String coupleNo;

    private String role;

}
