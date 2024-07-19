package clovider.clovider_be.domain.lottery.repository;

import clovider.clovider_be.domain.lottery.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long> {
}
