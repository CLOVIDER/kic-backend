package clovider.clovider_be.domain.recruit;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.enums.AgeClass;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

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
    public void updateFromDTO(RecruitCreateRequestDTO.RecruitClassCreateRequestDTO dto) {
        this.ageClass = dto.getAgeClass();
        this.recruitStartDt = dto.getRecruitStartDt();
        this.recruitEndDt = dto.getRecruitEndDt();
        this.recruitCnt = dto.getRecruitCnt();
        this.firstStartDt = dto.getFirstStartDt();
        this.firstEndDt = dto.getFirstEndDt();
        this.secondStartDt = dto.getSecondStartDt();
        this.secondEndDt = dto.getSecondEndDt();
        this.workYearsUsage = dto.getWorkYearsUsage();
        this.isSingleParentUsage = dto.getIsSingleParentUsage();
        this.childrenCntUsage = dto.getChildrenCntUsage();
        this.isDisabilityUsage = dto.getIsDisabilityUsage();
        this.isDualIncomeUsage = dto.getIsDualIncomeUsage();
        this.isEmployeeCoupleUsage = dto.getIsEmployeeCoupleUsage();
        this.isSiblingUsage = dto.getIsSiblingUsage();
    }
}