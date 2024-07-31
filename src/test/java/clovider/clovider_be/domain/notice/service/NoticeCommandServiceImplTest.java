package clovider.clovider_be.domain.notice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.repository.NoticeRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoticeCommandServiceImplTest {

    @InjectMocks
    private NoticeCommandServiceImpl noticeCommandService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeQueryService noticeQueryService;

    @Mock
    private EmployeeQueryService employeeQueryService;

    private Employee admin;
    private Notice notice;

    @BeforeEach
    void setup() {
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
    }

    private NoticeRequest createNoticeRequest(String title, String content, List<String> imageUrls) throws NoSuchFieldException, IllegalAccessException {
        NoticeRequest noticeRequest = new NoticeRequest();
        setField(noticeRequest, "title", title);
        setField(noticeRequest, "content", content);
        setField(noticeRequest, "imageUrls", imageUrls);
        return noticeRequest;
    }

    private void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    @Test
    @DisplayName("공지사항 작성 테스트")
    void createNotice() throws NoSuchFieldException, IllegalAccessException {
        // given
        NoticeRequest noticeRequest = createNoticeRequest(
                "공지사항 제목",
                "공지사항 내용",
                List.of("http://example.com/new_image1.jpg", "http://example.com/new_image2.jpg")
        );

        when(noticeRepository.save(any(Notice.class))).thenReturn(notice);

        // when
        CustomResult result = noticeCommandService.createNotice(admin, noticeRequest);

        // then
        assertEquals(1L, result.getId());
        verify(noticeRepository).save(any(Notice.class));
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    void updateNotice() throws NoSuchFieldException, IllegalAccessException {
        // given
        NoticeRequest noticeRequest = createNoticeRequest(
                "수정된 제목",
                "수정된 내용",
                List.of("http://example.com/new_image1.jpg", "http://example.com/new_image2.jpg")
        );

        when(noticeQueryService.findById(1L)).thenReturn(notice);

        // when
        NoticeUpdateResponse result = noticeCommandService.updateNotice(1L, noticeRequest);

        // then
        assertEquals(1L, result.getNoticeId());
        verify(noticeQueryService).findById(1L);
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void deleteNotice() {
        // given
        long noticeId = 1L;

        // when
        String result = noticeCommandService.deleteNotice(noticeId);

        // then
        assertEquals("공지사항 삭제에 성공했습니다.", result);
        verify(noticeRepository).deleteById(noticeId);
    }
}