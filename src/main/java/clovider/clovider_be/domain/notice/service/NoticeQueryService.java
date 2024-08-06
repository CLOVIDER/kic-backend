package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface NoticeQueryService {

    Notice findById(Long id);

    NoticeResponse getNotice(Long id, HttpServletRequest request, HttpServletResponse response);

    Page<NoticeResponse> getAllNotices(Pageable pageable,SearchType type, String keyword);

    List<NoticeTop3> getTop3Notices();
}
