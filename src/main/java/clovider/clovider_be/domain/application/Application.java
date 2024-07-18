package clovider.clovider_be.domain.application;

import clovider.clovider_be.domain.application.dto.ApplicationDto;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.employee.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    private Long id;

    // 임시저장을 위해 모든 컬럼 기본값 지정, 가중치에 모두 포함이 안되는 기본값으로 설정
    @Column(nullable = false)
    private Integer workYears = 0;

    @Column(nullable = false)
    private Boolean singleParent = false;

    @Column(nullable = false)
    private Integer childrenCnt = 1;

    @Column(nullable = false)
    private Boolean disability = false;

    @Column(nullable = false)
    private Boolean dualIncome = false;

    @Column(nullable = false)
    private Boolean employeeCouple = false;

    @Column(nullable = false)
    private Boolean sibling = false;

    @Column
    private Boolean tempSave = false; //default : 임시저장이 아닌 일반 저장 상태

    private String childName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Builder
    public Application(ApplicationDto applicationDto) {
        this.workYears = applicationDto.getWorkYears();
        this.singleParent = applicationDto.getSingleParent();
        this.childrenCnt = applicationDto.getChildrenCnt();
        this.disability = applicationDto.getDisability();
        this.dualIncome = applicationDto.getDualIncome();
        this.employeeCouple = applicationDto.getEmployeeCouple();
        this.sibling = applicationDto.getSibling();
    }
}