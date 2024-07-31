package clovider.clovider_be.domain.application;

import clovider.clovider_be.domain.application.dto.ApplicationRequest;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.document.Document;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.lottery.Lottery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "application_tb")
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    private Long id;

    // 임시저장을 위해 모든 컬럼 기본값 지정, 가중치에 모두 포함이 안되는 기본값으로 설정
    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer workYears;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isSingleParent;

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer childrenCnt;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isDisability;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isDualIncome;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isEmployeeCouple;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isSibling;

    @Column(length = 1)
    @ColumnDefault("'0'")
    private Character isTemp; //default : 임시저장이 아닌 일반 저장 상태

    private String childNm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @ColumnDefault("'WAIT")
    private Accept isAccept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lottery> lotteries;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    public void update(ApplicationRequest applicationRequest) {
        this.isSingleParent = applicationRequest.getIsSingleParent();
        this.childrenCnt = applicationRequest.getChildrenCnt();
        this.isDisability = applicationRequest.getIsDisability();
        this.isDualIncome = applicationRequest.getIsDualIncome();
        this.isEmployeeCouple = applicationRequest.getIsEmployeeCouple();
        this.isSibling = applicationRequest.getIsSibling();
        this.childNm = applicationRequest.getChildNm();
        this.isTemp = 0;
    }

    public void isAccept(Accept accept) {
        this.isAccept = accept;
    }
}