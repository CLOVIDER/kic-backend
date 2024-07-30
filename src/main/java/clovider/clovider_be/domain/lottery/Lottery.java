package clovider.clovider_be.domain.lottery;

import clovider.clovider_be.domain.application.Application;
import clovider.clovider_be.domain.common.BaseTimeEntity;
import clovider.clovider_be.domain.enums.Accept;
import clovider.clovider_be.domain.enums.Result;
import clovider.clovider_be.domain.recruit.Recruit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "lottery_tb")
public class Lottery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lottery_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    @ColumnDefault("'0'")
    private Integer rankNo;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Result result;

    @Setter
    @Column(nullable = false, length = 1)
    @ColumnDefault("'0'")
    private Character isRegistry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'WAIT'")
    private Accept isAccept;
    
}
