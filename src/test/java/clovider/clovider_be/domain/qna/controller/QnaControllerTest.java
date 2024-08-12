package clovider.clovider_be.domain.qna.controller;

import static clovider.clovider_be.domain.utils.TestUtils.setField;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.service.QnaCommandService;
import clovider.clovider_be.domain.qna.service.QnaQueryService;
import clovider.clovider_be.domain.utils.TestUtils;
import clovider.clovider_be.domain.utils.WithMockEmployee;
import clovider.clovider_be.global.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
        controllers = QnaController.class
)
@MockBean(JpaMetamodelMappingContext.class)
class QnaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QnaCommandService qnaCommandService;

    @MockBean
    private QnaQueryService qnaQueryService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private EmployeeQueryService employeeQueryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee admin;
    private Employee employee;

    @BeforeEach
    void setup() {
        admin = TestUtils.createEmployee(1L, "홍길동", "hong123", "securePassword", "E001", LocalDate.of(2022, 1, 1), "IT", Role.ADMIN);
        employee = TestUtils.createEmployee(2L, "직원1", "admin1", "admin2", "E002", LocalDate.of(2023, 1, 1), "IT", Role.EMPLOYEE);
    }

    private Qna createQna() throws NoSuchFieldException, IllegalAccessException {
        return TestUtils.createQna(1L, employee);
    }


    @Test
    @DisplayName("QNA 생성 테스트")
    @WithMockEmployee
    public void createQnaTest() throws Exception {
        // given
        Qna qna = createQna();
        QnaCreateRequest qnaCreateRequest = new QnaCreateRequest();
        setField(qnaCreateRequest, "title", "수정된 제목");
        setField(qnaCreateRequest, "question", "수정된 문제");
        setField(qnaCreateRequest, "isVisibility", '0');
        String jsonBody = objectMapper.writeValueAsString(qnaCreateRequest);

        CustomResult customResult = CustomResult.toCustomResult(qna.getId());

        when(qnaCommandService.createQna(any(Employee.class), any(QnaCreateRequest.class))).thenReturn(customResult);

        // when & then
        mockMvc.perform(post("/api/qnas").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L));

        verify(qnaCommandService).createQna(any(Employee.class), any(QnaCreateRequest.class));
    }

    @Test
    @DisplayName("QNA 수정 테스트")
    @WithMockEmployee
    public void updateQnaTest() throws Exception {
        // given
        Qna qna = createQna();
        QnaCreateRequest qnaCreateRequest = new QnaCreateRequest();
        setField(qnaCreateRequest, "title", "수정된 제목");
        setField(qnaCreateRequest, "question", "수정된 문제");
        setField(qnaCreateRequest, "isVisibility", '0');
        String jsonBody = objectMapper.writeValueAsString(qnaCreateRequest);

        QnaUpdateResponse qnaUpdateResponse = QnaUpdateResponse.of(qna.getId());

        when(qnaCommandService.updateQna(eq(qna.getId()), any(QnaCreateRequest.class))).thenReturn(qnaUpdateResponse);

        // when & then
        mockMvc.perform(patch("/api/qnas/{qnaId}", qna.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.qnaId").value(1L));

        verify(qnaCommandService).updateQna(eq(qna.getId()), any(QnaCreateRequest.class));
    }

    @Test
    @DisplayName("QNA 삭제 테스트")
    @WithMockEmployee
    public void deleteQnaTest() throws Exception {
        // given
        Qna qna = createQna();

        when(qnaCommandService.deleteQna(eq(qna.getId()))).thenReturn("qna 삭제에 성공하였습니다.");

        // when & then
        mockMvc.perform(delete("/api/qnas/{qnaId}", qna.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("qna 삭제에 성공하였습니다."));

        verify(qnaCommandService).deleteQna(eq(qna.getId()));
    }

    @Test
    @DisplayName("QNA 상세 조회 테스트")
    @WithMockEmployee
    public void getQnaTest() throws Exception {
        // given
        Qna qna = createQna();

        when(qnaQueryService.getQna(any(Employee.class),eq(qna.getId()))).
                thenReturn(DetailedQnaResponse.fromQna(qna,employee.getId()));

        // when & then
        mockMvc.perform(get("/api/qnas/{qnaId}", qna.getId()).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.qnaId").value(1L))
                .andExpect(jsonPath("$.result.title").value("문제입니다"))
                .andExpect(jsonPath("$.result.question").value("문제가 있어요"));

        verify(qnaQueryService).getQna(any(Employee.class),eq(qna.getId()));
    }

    @Test
    @DisplayName("QNA 전체 목록 조회 테스트")
    @WithMockEmployee
    public void getAllQna() throws Exception {
        // given
        SearchType searchType = SearchType.TITLE;
        String keyword = "문제";
        PageRequest pageRequest = PageRequest.of(0, 5);
        Qna qna1 = createQna();
        Qna qna2 = createQna();
        Qna qna3 = createQna();

        List<BaseQnaResponse> qnaResponses = Arrays.asList(
                BaseQnaResponse.fromQna(qna1),
                BaseQnaResponse.fromQna(qna2),
                BaseQnaResponse.fromQna(qna3)
        );

        Page<BaseQnaResponse> page = new PageImpl<>(qnaResponses, pageRequest, qnaResponses.size());

        when(qnaQueryService.getAllQnas(any(),any(),any())).thenReturn(page);

        // when & then
        mockMvc.perform(get("/api/qnas")
                        .param("searchType", String.valueOf(searchType)) // 쿼리 파라미터 추가
                        .param("keyword", keyword)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalPage").value(1))
                .andExpect(jsonPath("$.result.totalElements").value(3))
                .andExpect(jsonPath("$.result.size").value(5))
                .andExpect(jsonPath("$.result.currPage").value(0));
    }

    @Test
    @DisplayName("QNA 답변 수정 테스트")
    @WithMockEmployee
    public void updateAnswer() throws Exception {
        // given
        Qna qna1 = createQna();

        QnaAnswerRequest qnaAnswerRequest = new QnaAnswerRequest();
        setField(qnaAnswerRequest,"answer","답변입니다.");
        String jsonBody = objectMapper.writeValueAsString(qnaAnswerRequest);

        when(qnaCommandService.updateAnswer(any(Employee.class),eq(qna1.getId()),any(QnaAnswerRequest.class)))
                .thenReturn(QnaUpdateResponse.of(qna1.getId()));

        // when & then
        mockMvc.perform(patch("/api/qnas/admin/{qnaId}",qna1.getId())
                        .with(csrf())
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}