package clovider.clovider_be.domain.recruit;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.lottery.Lottery;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "recruit")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long recruitId;

    @Column(name = "recruit_start_date", nullable = false)
    private LocalDateTime recruitStartDate;

    @Column(name = "recruit_end_date", nullable = false)
    private LocalDateTime recruitEndDate;

    @Column(name = "recruit_cnt", nullable = false)
    private Integer recruitCnt;

    @Column(name = "age_range", length = 20, nullable = false)
    private String ageRange;

    @Column(name = "first_start_date", nullable = false)
    private LocalDateTime firstStartDate;

    @Column(name = "first_end_date", nullable = false)
    private LocalDateTime firstEndDate;

    @Column(name = "second_start_date", nullable = false)
    private LocalDateTime secondStartDate;

    @Column(name = "second_end_date", nullable = false)
    private LocalDateTime secondEndDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "kdg_id", nullable = false)
    private Long kdgId;

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lottery> lotteries;

}