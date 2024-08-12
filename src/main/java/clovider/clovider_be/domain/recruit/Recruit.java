package clovider.clovider_be.domain.recruit;

import clovider.clovider_be.domain.admin.dto.AdminResponse.RecruitClassInfo;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitResponse.RecruitDateAndWeightInfo;
import clovider.clovider_be.domain.recruit.dto.RecruitUpdateRequestDTO;
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
@Setter
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

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AgeClass ageClass;

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

    public void changeKindergarten(Kindergarten kindergarten){
        this.kindergarten = kindergarten;
    }

    // 생성 메서드
    public static Recruit createRecruit(RecruitCreateRequestDTO.RecruitClassCreateRequestDTO requestDTO, Kindergarten kindergarten) {
        return Recruit.builder()
                .recruitStartDt(requestDTO.getRecruitStartDt())
                .recruitEndDt(requestDTO.getRecruitEndDt())
                .recruitCnt(requestDTO.getRecruitCnt())
                .ageClass(requestDTO.getAgeClass())
                .firstStartDt(requestDTO.getFirstStartDt())
                .firstEndDt(requestDTO.getFirstEndDt())
                .secondStartDt(requestDTO.getSecondStartDt())
                .secondEndDt(requestDTO.getSecondEndDt())
                .kindergarten(kindergarten)
                .workYearsUsage(requestDTO.getWorkYearsUsage())
                .isSingleParentUsage(requestDTO.getIsSingleParentUsage())
                .childrenCntUsage(requestDTO.getChildrenCntUsage())
                .isDisabilityUsage(requestDTO.getIsDisabilityUsage())
                .isDualIncomeUsage(requestDTO.getIsDualIncomeUsage())
                .isEmployeeCoupleUsage(requestDTO.getIsEmployeeCoupleUsage())
                .isSiblingUsage(requestDTO.getIsSiblingUsage())
                .build();
    }

    public void updateRecruitDetails(RecruitUpdateRequestDTO dto) {
        this.ageClass = dto.getAgeClass();
        this.recruitStartDt = dto.getRecruitStartDt();
        this.recruitEndDt = dto.getRecruitEndDt();
        this.recruitCnt = dto.getRecruitCnt();
        this.firstStartDt = dto.getFirstStartDt();
        this.firstEndDt = dto.getFirstEndDt();
        this.secondStartDt = dto.getSecondStartDt();
        this.secondEndDt = dto.getSecondEndDt();

        RecruitResponse.RecruitWeightInfo weightInfo = dto.getRecruitWeightInfo();
        this.workYearsUsage = weightInfo.getWorkYearsUsage();
        this.isSingleParentUsage = weightInfo.getIsSingleParentUsage();
        this.childrenCntUsage = weightInfo.getChildrenCntUsage();
        this.isDisabilityUsage = weightInfo.getIsDisabilityUsage();
        this.isDualIncomeUsage = weightInfo.getIsDualIncomeUsage();
        this.isEmployeeCoupleUsage = weightInfo.getIsEmployeeCoupleUsage();
        this.isSiblingUsage = weightInfo.getIsSiblingUsage();
    }

    // 모집 생성 메서드
    public static Recruit createRecruit( RecruitClassInfo classInfo,
            RecruitDateAndWeightInfo recruitDateAndWeightInfo, Kindergarten kindergarten, AgeClass ageClass) {
        return Recruit.builder()
                .recruitStartDt(recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitStartDt())
                .recruitEndDt(recruitDateAndWeightInfo.getRecruitDateInfo().getRecruitEndDt())
                .recruitCnt(classInfo.getRecruitCnt())
                .ageClass(ageClass)
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
                .isEmployeeCoupleUsage(recruitDateAndWeightInfo.getRecruitWeightInfo().getIsEmployeeCoupleUsage())
                .isSiblingUsage(recruitDateAndWeightInfo.getRecruitWeightInfo().getIsSiblingUsage())
                .build();
    }
}
