package clovider.clovider_be.domain.qna.service;

import static clovider.clovider_be.domain.utils.TestUtils.setField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.domain.utils.TestUtils;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QnaCommandServiceImplTest {
    @InjectMocks
    private QnaCommandServiceImpl qnaCommandService;
    @Mock
    QnaRepository qnaRepository;
    @Mock
    private QnaQueryService qnaQueryService;
    @Mock
    private EmployeeQueryService employeeQueryService;

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
    public void createQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();
        QnaCreateRequest updateRequest = new QnaCreateRequest();
        setField(updateRequest, "title", "수정된 제목");
        setField(updateRequest, "question", "수정된 문제");
        setField(updateRequest, "isVisibility", '0');
        setField(updateRequest,"imageUrls", List.of("123","456"));

        when(qnaRepository.save(any(Qna.class))).thenReturn(qna);

        // when
        CustomResult result = qnaCommandService.createQna(employee, updateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(qna.getId());

        verify(qnaRepository).save(any(Qna.class));
    }

    @Test
    @DisplayName("QNA 수정 테스트")
    public void updateQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();
        QnaCreateRequest updateRequest = new QnaCreateRequest();
        setField(updateRequest, "title", "수정된 제목");
        setField(updateRequest, "question", "수정된 문제");
        setField(updateRequest, "isVisibility", '0');
        TestUtils.setField(updateRequest,"imageUrls", List.of("123","456"));

        when(qnaQueryService.findById(qna.getId())).thenReturn(qna);

        // when
        QnaUpdateResponse qnaUpdateResponse = qnaCommandService.updateQna(qna.getId(),
                updateRequest);

        // when
        assertThat(qnaUpdateResponse).isNotNull();
        assertThat(qnaUpdateResponse.getQnaId()).isEqualTo(qna.getId());
        assertThat(qna.getTitle()).isEqualTo("수정된 제목");
        assertThat(qna.getQuestion()).isEqualTo("수정된 문제");
        assertThat(qna.getIsVisibility()).isEqualTo('0');

        verify(qnaQueryService).findById(qna.getId());
    }

    @Test
    @DisplayName("QNA 삭제 테스트")
    public void deleteQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();

        // when
        qnaCommandService.deleteQna(qna.getId());

        // then
        verify(qnaRepository).deleteById(qna.getId());
    }

    @Test
    @DisplayName("QNA 답변 수정 테스트")
    public void updateAnswerTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();
        QnaAnswerRequest answerRequest = new QnaAnswerRequest();
        setField(answerRequest,"answer","답변입니다.");

        when(qnaQueryService.findById(eq(qna.getId()))).thenReturn(qna);

        // when
        QnaUpdateResponse response = qnaCommandService.updateAnswer(admin, qna.getId(), answerRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getQnaId()).isEqualTo(qna.getId());
        assertThat(qna.getAnswer()).isEqualTo("답변입니다.");
        assertThat(qna.getAdmin()).isEqualTo(admin);

        verify(qnaQueryService).findById(eq(qna.getId()));
    }
}