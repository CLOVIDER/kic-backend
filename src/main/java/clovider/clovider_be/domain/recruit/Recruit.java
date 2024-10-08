package clovider.clovider_be.domain.recruit;

import clovider.clovider_be.domain.admin.dto.AdminRequest;
import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitClassInfo;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "recruit_tb")
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime recruitStartDt;

    @Column(nullable = false)
    private LocalDateTime recruitEndDt;

    @Column(nullable = false)
    private Integer recruitCnt;

    @Column(nullable = false)
    private Integer ageClass;

    @Column(nullable = false)
    private LocalDateTime firstStartDt;

    @Column(nullable = false)
    private LocalDateTime firstEndDt;

    @Column(nullable = false)
    private LocalDateTime secondStartDt;

    @Column(nullable = false)
    private LocalDateTime secondEndDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kindergarten_id", nullable = false)
    private Kindergarten kindergarten;

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lottery> lotteries;

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Character workYearsUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isSingleParentUsage;

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Character childrenCntUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isDisabilityUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isDualIncomeUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isEmployeeCoupleUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isSiblingUsage;

    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isDrew;

    public void changeKindergarten(Kindergarten kindergarten) {
        this.kindergarten = kindergarten;
    }


    public void updateRecruitDetails(RecruitClassInfo classInfo,
            RecruitDateAndWeightInfo recruitDateAndWeightInfo) {
        this.ageClass = classInfo.getAgeClass();
        this.recruitCnt = classInfo.getRecruitCnt();

        // 모집 기간 정보 업데이트
        this.recruitStartDt = recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitStartDt();
        this.recruitEndDt = recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitEndDt();
        this.firstStartDt = recruitDateAndWeightInfo.getRecruitDateInfo().getFirstStartDt();
        this.firstEndDt = recruitDateAndWeightInfo.getRecruitDateInfo().getFirstEndDt();
        this.secondStartDt = recruitDateAndWeightInfo.getRecruitDateInfo().getSecondStartDt();
        this.secondEndDt = recruitDateAndWeightInfo.getRecruitDateInfo().getSecondEndDt();

        // 가중치 정보 업데이트
        this.workYearsUsage = recruitDateAndWeightInfo.getRecruitWeightInfo().getWorkYearsUsage();
        this.isSingleParentUsage = recruitDateAndWeightInfo.getRecruitWeightInfo()
                .getIsSingleParentUsage();
        this.childrenCntUsage = recruitDateAndWeightInfo.getRecruitWeightInfo()
                .getChildrenCntUsage();
        this.isDisabilityUsage = recruitDateAndWeightInfo.getRecruitWeightInfo()
                .getIsDisabilityUsage();
        this.isDualIncomeUsage = recruitDateAndWeightInfo.getRecruitWeightInfo()
                .getIsDualIncomeUsage();
        this.isEmployeeCoupleUsage = recruitDateAndWeightInfo.getRecruitWeightInfo()
                .getIsEmployeeCoupleUsage();
        this.isSiblingUsage = recruitDateAndWeightInfo.getRecruitWeightInfo().getIsSiblingUsage();
    }


    // 모집 생성 메서드
    public static Recruit createRecruit(AdminRequest.RecruitClassInfo classInfo,
            RecruitDateAndWeightInfo recruitDateAndWeightInfo, Kindergarten kindergarten) {
        return Recruit.builder()
                .recruitStartDt(recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitStartDt())
                .recruitEndDt(recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitEndDt())
                .recruitCnt(classInfo.getRecruitCnt())
                .ageClass(classInfo.getAgeClass())
                .firstStartDt(recruitDateAndWeightInfo.getRecruitDateInfo().getFirstStartDt())
                .firstEndDt(recruitDateAndWeightInfo.getRecruitDateInfo().getFirstEndDt())
                .secondStartDt(recruitDateAndWeightInfo.getRecruitDateInfo().getSecondStartDt())
                .secondEndDt(recruitDateAndWeightInfo.getRecruitDateInfo().getSecondEndDt())
                .kindergarten(kindergarten)
                .workYearsUsage(recruitDateAndWeightInfo.getRecruitWeightInfo().getWorkYearsUsage())
                .isSingleParentUsage(recruitDateAndWeightInfo.getRecruitWeightInfo()
                        .getIsSingleParentUsage())
                .childrenCntUsage(recruitDateAndWeightInfo.getRecruitWeightInfo()
                        .getChildrenCntUsage())
                .isDisabilityUsage(recruitDateAndWeightInfo.getRecruitWeightInfo()
                        .getIsDisabilityUsage())
                .isDualIncomeUsage(recruitDateAndWeightInfo.getRecruitWeightInfo()
                        .getIsDualIncomeUsage())
                .isEmployeeCoupleUsage(
                        recruitDateAndWeightInfo.getRecruitWeightInfo().getIsEmployeeCoupleUsage())
                .isSiblingUsage(recruitDateAndWeightInfo.getRecruitWeightInfo().getIsSiblingUsage())
                .isDrew('0')
                .build();
    }

    public void setIsDrew(char c) {
        this.isDrew = c;
    }
}