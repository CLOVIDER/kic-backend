package clovider.clovider_be.domain.notice.repository;

import clovider.clovider_be.domain.notice.Notice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findTop3ByOrderByIdDesc();
}
