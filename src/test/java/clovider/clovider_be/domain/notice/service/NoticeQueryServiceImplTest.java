package clovider.clovider_be.domain.notice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeTop3;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import clovider.clovider_be.global.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class NoticeQueryServiceImplTest {

    @InjectMocks
    private NoticeQueryServiceImpl noticeQueryService;

    @Mock
    private NoticeRepository noticeRepository;

    private Employee admin;
    private Notice notice;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        admin = Employee.builder()
                .nameKo("홍길동")
                .accountId("hong123")
                .password("securePassword")
                .employeeNo("E001")
                .joinDt(LocalDate.of(2022, 1, 1))
                .dept("IT")
                .role(Role.ADMIN)
                .build();

        notice = Notice.builder()
                .id(1L)
                .title("공지사항 제목")
                .content("공지사항 내용")
                .hits(0)
                .images(new ArrayList<>())
                .build();

        setField(notice, "createdAt", LocalDateTime.now().minusDays(2));
        setField(notice, "updatedAt", LocalDateTime.now());
    }

    private void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> superclass = obj.getClass().getSuperclass();
        Field field = superclass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    @DisplayName("공지사항 ID로 조회 테스트")
    void findById() {
        // given
        when(noticeRepository.findById(1L)).thenReturn(java.util.Optional.of(notice));

        // when
        Notice result = noticeQueryService.findById(1L);

        // then
        assertEquals(notice, result);
        verify(noticeRepository).findById(1L);
    }

    @Test
    @DisplayName("공지사항 ID로 조회 시 예외 발생 테스트")
    void findByIdThrowsException() {
        // given
        when(noticeRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // when + then
        assertThrows(ApiException.class, () -> noticeQueryService.findById(1L));
    }

    @Test
    @DisplayName("공지사항 조회 테스트")
    void getNotice() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(noticeRepository.findById(1L)).thenReturn(java.util.Optional.of(notice));

        // when
        NoticeResponse result = noticeQueryService.getNotice(1L, request, response);

        // then
        assertEquals(result.getNoticeId(), notice.getId());
        verify(noticeRepository).findById(1L);
    }

    @Test
    @DisplayName("모든 공지사항 조회 테스트")
    void getAllNotices() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 2);
        List<NoticeResponse> noticeResponses = List.of(
                NoticeResponse.toNoticeResponse(notice),
                NoticeResponse.toNoticeResponse(notice),
                NoticeResponse.toNoticeResponse(notice));

        Page<NoticeResponse> noticePage = new PageImpl<>(noticeResponses, pageRequest, noticeResponses.size());

        when(noticeRepository.searchNotices(pageRequest,SearchType.TITLE, "공지사항")).thenReturn(noticePage);

        // when
        Page<NoticeResponse> result = noticeQueryService.getAllNotices(pageRequest,
                SearchType.TITLE, "공지사항");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent()).containsExactlyElementsOf(noticeResponses);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("최신 3개 공지사항 조회 테스트")
    void getTop3Notices() {
        // given
        List<Notice> topNotices = List.of(notice, notice, notice);
        when(noticeRepository.findTop3ByOrderByIdDesc()).thenReturn(topNotices);

        // when
        List<NoticeTop3> result = noticeQueryService.getTop3Notices();

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(noticeRepository).findTop3ByOrderByIdDesc();
    }

}