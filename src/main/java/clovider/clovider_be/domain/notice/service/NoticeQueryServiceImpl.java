package clovider.clovider_be.domain.notice.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeQueryServiceImpl implements NoticeQueryService {

    private final NoticeRepository noticeRepository;

    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));
    }

    @Cacheable(value = "notices", key = "#noticeId")
    @Transactional
    public NoticeResponse getNotice(Long noticeId, HttpServletRequest request, HttpServletResponse response) {
        Notice foundNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOTICE_NOT_FOUND));

        handleViewCookie(foundNotice, noticeId, request, response);

        return NoticeResponse.toNoticeResponse(foundNotice);
    }

    @Override
    public CustomPage<NoticeResponse> getAllNotices(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Notice> noticePage = noticeRepository.findAll(pageRequest);

        Page<NoticeResponse> noticeResponsePage = noticePage.map(NoticeResponse::toNoticeResponse);
        return new CustomPage<>(noticeResponsePage);
    }

    @Override
    public List<NoticeTop3> getTop3Notices() {
        return NoticeTop3.from(noticeRepository.findTop3ByOrderByIdDesc());
    }

    @Override
    public List<NoticeResponse> searchNotices(SearchType type, String keyword) {
        return noticeRepository.searchNotices(type, keyword);
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
