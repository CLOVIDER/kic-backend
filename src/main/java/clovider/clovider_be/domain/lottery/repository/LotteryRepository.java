package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.lottery.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryRepository extends JpaRepository<Lottery, Long>, LotteryRepositoryCustom {
}