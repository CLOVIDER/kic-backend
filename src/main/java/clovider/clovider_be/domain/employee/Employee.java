package clovider.clovider_be.domain.employee;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(nullable = false)
    private String nameKo;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String employeeNo;

    @Column(nullable = false)
    private LocalDate joinDate;

    @Column(nullable = false)
    private String dept;

    private String coupleNo;

    @Enumerated(EnumType.STRING)
    private Role role;

}
