package clovider.clovider_be.domain.notice.repository;

import clovider.clovider_be.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
