package clovider.clovider_be.domain.lottery;

import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.recruit.Recruit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Lottery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lottery_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

//    @ManyToOne
//    @JoinColumn(name = "application_id", nullable = false)
//    private Application application;

    @Column(nullable = false)
    private Integer rank;

    @Column(length = 15, nullable = false)
    private String result;

    @Column(nullable = false)
    private Boolean registry;

    @Column(nullable = false)
    private Boolean accept;
    
}