package clovider.clovider_be.domain.notice.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.utils.WithMockAdmin;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.service.NoticeCommandService;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.global.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = NoticeController.class
)
@MockBean(JpaMetamodelMappingContext.class)
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeCommandService noticeCommandService;

    @MockBean
    private NoticeQueryService noticeQueryService;

    @MockBean
    private EmployeeQueryService employeeQueryService;

    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee admin;
    private Notice notice;

    @BeforeEach
    void setup() {
        admin = Employee.builder()
                .id(1L)
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
                .admin(admin)
                .hits(0)
                .images(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("공지사항 세부 조회 테스트")
    @WithMockAdmin
    void getNotice() throws Exception {
        // given
        NoticeResponse noticeResponse = NoticeResponse.toNoticeResponse(notice);
        when(noticeQueryService.getNotice(eq(notice.getId()), any(HttpServletRequest.class), any(
                HttpServletResponse.class)))
                .thenReturn(noticeResponse);

        // when & then
        mockMvc.perform(get("/api/notices/{noticeId}", notice.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.noticeId").value(1))
                .andExpect(jsonPath("$.result.title").value("공지사항 제목"))
                .andExpect(jsonPath("$.result.content").value("공지사항 내용"));

        verify(noticeQueryService).getNotice(
                eq(notice.getId()),
                any(HttpServletRequest.class),
                any(HttpServletResponse.class)
        );
    }

    @Test
    @DisplayName("공지사항 작성 테스트")
    @WithMockAdmin
    void createNotice() throws Exception {
        // given
        String title = "햇님 어린이집 07회차 모집 안내";
        String content = "햇님 어린이집 07회자 모집 안내입니다. ~~~~";
        List<String> imageUrls = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg");
        NoticeRequest noticeRequest = createTestNoticeRequest(title, content, imageUrls);
        String jsonBody = objectMapper.writeValueAsString(noticeRequest);

        CustomResult customResult = CustomResult.toCustomResult(notice.getId());
        when(noticeCommandService.createNotice(any(Employee.class), any(NoticeRequest.class))).thenReturn(customResult);

        // when & then
        mockMvc.perform(post("/api/admin/notices").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L));

        verify(noticeCommandService).createNotice(any(Employee.class), argThat(request ->
                request.getTitle().equals("햇님 어린이집 07회차 모집 안내") &&
                        request.getContent().equals("햇님 어린이집 07회자 모집 안내입니다. ~~~~") &&
                        request.getImageUrls().equals(List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"))
        ));

    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    @WithMockAdmin
    void updateNotice() throws Exception {
        // given
        String title = "햇님 어린이집 07회차 모집 안내";
        String content = "햇님 어린이집 07회자 모집 안내입니다. ~~~~";
        List<String> imageUrls = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg");
        NoticeRequest noticeRequest = createTestNoticeRequest(title, content, imageUrls);
        String jsonBody = objectMapper.writeValueAsString(noticeRequest);

        NoticeUpdateResponse noticeUpdateResponse =NoticeUpdateResponse.builder()
                .noticeId(notice.getId())
                .build();

        when(noticeCommandService.updateNotice(any(Long.class), any(NoticeRequest.class))).thenReturn(noticeUpdateResponse);

        // when & then
        mockMvc.perform(patch("/api/admin/notices/{noticeId}",notice.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.noticeId").value(1L));

        verify(noticeCommandService).updateNotice(any(Long.class), any(NoticeRequest.class));
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    @WithMockAdmin
    void deleteNotice() throws Exception {
        when(noticeCommandService.deleteNotice(any(Long.class))).thenReturn("공지사항 삭제에 성공했습니다.");

        // when & then
        mockMvc.perform(delete("/api/admin/notices/{noticeId}",notice.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("공지사항 삭제에 성공했습니다."));

        verify(noticeCommandService).deleteNotice(any(Long.class));
    }

    @Test
    @DisplayName("전체 공지사항 목록 조회")
    @WithMockAdmin
    void getAllNotices() throws Exception {
        // Given
        int page = 0;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page, size);
        SearchType type = SearchType.TITLE;
        String keyword = "테스트";

        List<NoticeResponse> noticeResponses = Arrays.asList(
                NoticeResponse.toNoticeResponse(notice),
                NoticeResponse.toNoticeResponse(notice),
                NoticeResponse.toNoticeResponse(notice));

        Page<NoticeResponse> noticePage = new PageImpl<>(noticeResponses, pageRequest, noticeResponses.size());

        when(noticeQueryService.getAllNotices(any(PageRequest.class), any(SearchType.class), any(String.class)))
                .thenReturn(noticePage);

        // when & then
        mockMvc.perform(get("/api/notices")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("type", type.name())
                .param("keyword", keyword)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.content", hasSize(3)))
                .andExpect(jsonPath("$.result.content[0].noticeId").value(1))
                .andExpect(jsonPath("$.result.content[0].title").value("공지사항 제목"))
                .andExpect(jsonPath("$.result.content[0].content").value("공지사항 내용"))
                .andExpect(jsonPath("$.result.content[0].hits").value(0))
                .andExpect(jsonPath("$.result.content[0].noticeImageList").isArray())
                .andExpect(jsonPath("$.result.content[0].noticeImageList").isEmpty())
                .andExpect(jsonPath("$.result.content[0].createdAt").value(nullValue()))
                .andExpect(jsonPath("$.result.totalPage").value(1))
                .andExpect(jsonPath("$.result.totalElements").value(3))
                .andExpect(jsonPath("$.result.size").value(3))
                .andExpect(jsonPath("$.result.currPage").value(0))
                .andExpect(jsonPath("$.result.hasNext").value(false))
                .andExpect(jsonPath("$.result.isFirst").value(true))
                .andExpect(jsonPath("$.result.isLast").value(true));

        verify(noticeQueryService).getAllNotices(any(PageRequest.class), eq(type), eq(keyword));
    }

    private NoticeRequest createTestNoticeRequest(String title, String content, List<String> imageUrls) {
        NoticeRequest noticeRequest = new NoticeRequest();
        setField(noticeRequest, "title", title);
        setField(noticeRequest, "content", content);
        setField(noticeRequest, "imageUrls", imageUrls);
        return noticeRequest;
    }


}