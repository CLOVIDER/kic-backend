package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import clovider.clovider_be.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeRepository noticeRepository;
    private final RedisUtil redisUtil;

    @Override
    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));
    }

    @Cacheable(value = "notices", key = "#noticeId")
    @Override
    public NoticeResponse getNotice(Long noticeId, HttpServletRequest request, HttpServletResponse response) {
        Notice foundNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));

        handleViewCookie(foundNotice, noticeId, request, response);

        return NoticeResponse.toNoticeResponse(foundNotice);
    }

    @Cacheable(value = "notices", key = "#noticeId")
    @Override
    public NoticeResponse getNotice(Employee employee, Long noticeId,
            HttpServletRequest request, HttpServletResponse response) {
        Notice foundNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));

        // 직원 ID를 사용하여 공지사항 조회 상태 관리
        if (!redisUtil.hasViewedNotice(noticeId, employee.getId())) {
            redisUtil.markNoticeAsViewed(noticeId, employee.getId());
            foundNotice.incrementHits(); // 조회수가 증가하는 로직
        }

        return NoticeResponse.toNoticeResponse(foundNotice);
    }

    @Override
    public Page<NoticeResponse> getAllNotices(Pageable pageable, SearchType type, String keyword) {
        return noticeRepository.searchNotices(pageable,type,keyword);
    }

    @Override
    public List<NoticeTop3> getTop3Notices() {
        return NoticeTop3.from(noticeRepository.findTop3ByOrderByIdDesc());
    }

    private void handleViewCookie(Notice foundNotice, Long noticeId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = getCookie(request.getCookies());

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + noticeId.toString() + "]")) {
                foundNotice.incrementHits();
                oldCookie.setValue(oldCookie.getValue() + "_[" + noticeId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            foundNotice.incrementHits();
            Cookie newCookie = new Cookie("noticeView", "[" + noticeId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

    private Cookie getCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("noticeView")) {
                    return cookie;
                }
            }
        }
        return null;
    }



}
