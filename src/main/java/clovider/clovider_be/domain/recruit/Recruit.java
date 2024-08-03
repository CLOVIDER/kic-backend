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
    private Integer workYearsUsage;

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



    // 정적 팩토리 메서드 추가
    public static Recruit createRecruit(RecruitCreateRequestDTO requestDTO, Kindergarten kindergarten) {
        Recruit recruit = new Recruit();
        recruit.recruitStartDt = requestDTO.getRecruitStartDt();
        recruit.recruitEndDt = requestDTO.getRecruitEndDt();
        recruit.recruitCnt = requestDTO.getRecruitCnt();
        recruit.ageClass = requestDTO.getAgeClass();
        recruit.firstStartDt = requestDTO.getFirstStartDt();
        recruit.firstEndDt = requestDTO.getFirstEndDt();
        recruit.secondStartDt = requestDTO.getSecondStartDt();
        recruit.secondEndDt = requestDTO.getSecondEndDt();
        recruit.kindergarten = kindergarten;
        recruit.workYearsUsage = requestDTO.getWorkYearsUsage();
        recruit.isSingleParentUsage = requestDTO.getIsSingleParentUsage();
        recruit.childrenCntUsage = requestDTO.getChildrenCntUsage();
        recruit.isDisabilityUsage = requestDTO.getIsDisabilityUsage();
        recruit.isDualIncomeUsage = requestDTO.getIsDualIncomeUsage();
        recruit.isEmployeeCoupleUsage = requestDTO.getIsEmployeeCoupleUsage();
        recruit.isSiblingUsage = requestDTO.getIsSiblingUsage();
        return recruit;
    }
}