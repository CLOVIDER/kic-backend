package clovider.clovider_be.domain.application;

import clovider.clovider_be.domain.application.dto.ApplicationUpdateDto;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.recruit.Recruit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "application_tb")
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    private Long id;

    // 임시저장을 위해 모든 컬럼 기본값 지정, 가중치에 모두 포함이 안되는 기본값으로 설정
    @Column(nullable = false)
    private Integer workYears = 0;

    @Column(nullable = false, length = 1)
    private Character isSingleParent = 0;

    @Column(nullable = false)
    private Integer childrenCnt = 1;

    @Column(nullable = false, length = 1)
    private Character isDisability = 0;

    @Column(nullable = false, length = 1)
    private Character isDualIncome = 0;

    @Column(nullable = false, length = 1)
    private Character isEmployeeCouple = 0;

    @Column(nullable = false, length = 1)
    private Character isSibling = 0;

    @Column(length = 1)
    private Character isTemp = 0; //default : 임시저장이 아닌 일반 저장 상태

    private String childName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;


//     @ManyToOne
//     @JoinColumn(name = "recruit_id")
//     private Recruit recruit;

//     @Builder
//     public Application(ApplicationDto applicationDto) {
//         this.workYears = applicationDto.getWorkYears();
//         this.isSingleParent = applicationDto.getIsSingleParent();
//         this.childrenCnt = applicationDto.getChildrenCnt();
//         this.isDisability = applicationDto.getIsDisability();
//         this.isDualIncome = applicationDto.getIsDualIncome();
//         this.isEmployeeCouple = applicationDto.getIsEmployeeCouple();
//         this.isSibling = applicationDto.getIsSibling();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    public void update(ApplicationUpdateDto applicationUpdateDto) {
        this.isSingleParent = applicationUpdateDto.getIsSingleParent();
        this.childrenCnt = applicationUpdateDto.getChildrenCnt();
        this.isDisability = applicationUpdateDto.getIsDisability();
        this.isDualIncome = applicationUpdateDto.getIsDualIncome();
        this.isEmployeeCouple = applicationUpdateDto.getIsEmployeeCouple();
        this.isSibling = applicationUpdateDto.getIsSibling();
        this.childName = applicationUpdateDto.getChildName();

    }
}