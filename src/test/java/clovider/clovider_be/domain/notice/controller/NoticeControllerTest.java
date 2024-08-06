package clovider.clovider_be.domain.notice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.notice.Notice;
import clovider.clovider_be.domain.notice.config.WithMockAdmin;
import clovider.clovider_be.domain.notice.dto.NoticeRequest;
import clovider.clovider_be.domain.notice.dto.NoticeResponse;
import clovider.clovider_be.domain.notice.dto.NoticeUpdateResponse;
import clovider.clovider_be.domain.notice.service.NoticeCommandService;
import clovider.clovider_be.domain.notice.service.NoticeQueryService;
import clovider.clovider_be.global.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        when(noticeQueryService.getNotice(eq(notice.getId()), any(), any())).thenReturn(noticeResponse);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/notices/{noticeId}", notice.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.noticeId").value(1))
                .andExpect(jsonPath("$.result.title").value("공지사항 제목"))
                .andExpect(jsonPath("$.result.content").value("공지사항 내용"));
    }

    @Test
    @DisplayName("공지사항 작성 테스트")
    @WithMockAdmin
    void createNotice() throws Exception {
        // given
        NoticeRequest noticeRequest = new NoticeRequest();
        setField(noticeRequest, "title", "햇님 어린이집 07회차 모집 안내");
        setField(noticeRequest, "content", "햇님 어린이집 07회자 모집 안내입니다. ~~~~");
        setField(noticeRequest, "imageUrls", List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        String jsonBody = objectMapper.writeValueAsString(noticeRequest);

        CustomResult customResult = CustomResult.toCustomResult(notice.getId());
        when(noticeCommandService.createNotice(any(), any(NoticeRequest.class))).thenReturn(customResult);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/notices").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L));

    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    @WithMockAdmin
    void updateNotice() throws Exception {
        // given
        NoticeRequest noticeRequest = new NoticeRequest();
        setField(noticeRequest, "title", "햇님 어린이집 07회차 모집 안내");
        setField(noticeRequest, "content", "햇님 어린이집 07회자 모집 안내입니다. ~~~~");
        setField(noticeRequest, "imageUrls", List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg"));

        String jsonBody = objectMapper.writeValueAsString(noticeRequest);

        NoticeUpdateResponse noticeUpdateResponse =NoticeUpdateResponse.builder()
                .noticeId(notice.getId())
                .build();

        when(noticeCommandService.updateNotice(any(), any())).thenReturn(noticeUpdateResponse);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/notices/{noticeId}",notice.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.noticeId").value(1L));
    }

}