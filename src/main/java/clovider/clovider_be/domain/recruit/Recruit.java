package clovider.clovider_be.domain.recruit;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.lottery.Lottery;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Recruit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime recruitStartDate;

    @Column(nullable = false)
    private LocalDateTime recruitEndDate;

    @Column(nullable = false)
    private Integer recruitCnt;

    @Column(length = 20, nullable = false)
    private String ageRange;

    @Column(nullable = false)
    private LocalDateTime firstStartDate;

    @Column(nullable = false)
    private LocalDateTime firstEndDate;

    @Column(nullable = false)
    private LocalDateTime secondStartDate;

    @Column(nullable = false)
    private LocalDateTime secondEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kindergarden_id", nullable = false)
    private Kindergarten kindergarten;

    @OneToMany(mappedBy = "recruit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lottery> lotteries;

}