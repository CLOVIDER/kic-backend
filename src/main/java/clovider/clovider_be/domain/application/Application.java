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
@Table(name = "application_td")
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    private Long id;

    // 임시저장을 위해 모든 컬럼 기본값 지정, 가중치에 모두 포함이 안되는 기본값으로 설정
    @Column(nullable = false)
    private Integer workYears = 0;

    @Column(nullable = false)
    private Boolean isSingleParent = false;

    @Column(nullable = false)
    private Integer childrenCnt = 1;

    @Column(nullable = false)
    private Boolean isDisability = false;

    @Column(nullable = false)
    private Boolean isDualIncome = false;

    @Column(nullable = false)
    private Boolean isEmployeeCouple = false;

    @Column(nullable = false)
    private Boolean isSibling = false;

    @Column
    private Boolean isTemp = false; //default : 임시저장이 아닌 일반 저장 상태

    private String childName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Builder
    public Application(ApplicationDto applicationDto) {
        this.workYears = applicationDto.getWorkYears();
        this.isSingleParent = applicationDto.getIsSingleParent();
        this.childrenCnt = applicationDto.getChildrenCnt();
        this.isDisability = applicationDto.getIsDisability();
        this.isDualIncome = applicationDto.getIsDualIncome();
        this.isEmployeeCouple = applicationDto.getIsEmployeeCouple();
        this.isSibling = applicationDto.getIsSibling();
    }
}