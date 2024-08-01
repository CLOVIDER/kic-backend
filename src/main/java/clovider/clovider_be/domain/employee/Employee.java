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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "employee_tb",
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueAccountId", columnNames = {"account_id"})
    }
)
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

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
