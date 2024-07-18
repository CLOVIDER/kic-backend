package clovider.clovider_be.domain.lottery;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.recruit.Recruit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lottery")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Lottery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lottery_id")
    private Long lotteryId;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

//    @ManyToOne
//    @JoinColumn(name = "application_id", nullable = false)
//    private Application application;

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Column(name = "result", length = 15, nullable = false)
    private String result;

    @Column(name = "registry", nullable = false)
    private Boolean registry;

    @Column(name = "accept", nullable = false)
    private Boolean accept;

    // Getters and Setters
    // ...
}