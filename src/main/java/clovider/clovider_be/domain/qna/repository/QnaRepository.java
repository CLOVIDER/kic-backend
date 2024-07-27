package clovider.clovider_be.domain.qna.repository;

import clovider.clovider_be.domain.qna.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    Integer countAllByAnswerIsNull();
}
