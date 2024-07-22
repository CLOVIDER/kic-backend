package clovider.clovider_be.domain.employee;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String nameKo;

    @Column(nullable = false, length = 40)
    private String accountId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String employeeNo;

    @Column(nullable = false)
    private LocalDate joinDt;

    @Column(nullable = false, length = 20)
    private String dept;

    @Column(length = 20)
    private String coupleNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
