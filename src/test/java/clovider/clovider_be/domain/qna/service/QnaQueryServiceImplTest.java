package clovider.clovider_be.domain.qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.domain.utils.TestUtils;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class QnaQueryServiceImplTest {
    @Mock
    private QnaRepository qnaRepository;
    @InjectMocks
    private QnaQueryServiceImpl qnaQueryService;
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
    @DisplayName("QNA ID 기반 조회 테스트")
    public void findQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();

        when(qnaRepository.findById(qna.getId())).thenReturn(Optional.of(qna));

        // when
        Qna foundQna = qnaQueryService.findById(qna.getId());

        // then
        verify(qnaRepository).findById(qna.getId());
        assertThat(foundQna).isNotNull();
        assertThat(foundQna.getId()).isEqualTo(qna.getId());
        assertThat(foundQna.getTitle()).isEqualTo("문제입니다");
    }

    @Test
    @DisplayName("QNA 상세조회 테스트")
    public void getQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();
        when(qnaRepository.findById(qna.getId())).thenReturn(Optional.of(qna));

        // when
        DetailedQnaResponse foundQna = qnaQueryService.getQna(employee, qna.getId());

        // then
        verify(qnaRepository).findById(qna.getId());
        assertThat(foundQna).isNotNull();
        assertThat(foundQna.getQnaId()).isEqualTo(qna.getId());
        assertThat(foundQna.getTitle()).isEqualTo(qna.getTitle());
        assertThat(foundQna.getAnswer()).isEqualTo(qna.getAnswer());
        assertThat(foundQna.getQuestion()).isEqualTo(qna.getQuestion());
    }

    @Test
    @DisplayName("QNA 리스트 조회 테스트")
    public void getAllQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        SearchType searchType = SearchType.TITLE;
        String keyword = "문제";
        Pageable pageable = PageRequest.of(0, 2);
        Qna qna1 = createQna();
        Qna qna2 = createQna();
        Qna qna3 = createQna();

        List<BaseQnaResponse> qnaResponses = Arrays.asList(
                BaseQnaResponse.fromQna(qna1),
                BaseQnaResponse.fromQna(qna2),
                BaseQnaResponse.fromQna(qna3)
        );

        Page<BaseQnaResponse> page = new PageImpl<>(qnaResponses, pageable, qnaResponses.size());

        when(qnaRepository.searchQnas(pageable, searchType, keyword)).thenReturn(page);

        // when
        Page<BaseQnaResponse> result = qnaQueryService.getAllQnas(pageable, searchType, keyword);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3L);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
    }

    @Test
    @DisplayName("답변되지 않은 QNA 개수 조회 테스트")
    public void getWaitQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna qna = createQna();
        Qna qna2 = createQna();
        Qna qna3 = createQna();

        when(qnaRepository.countAllByAnswerIsNull()).thenReturn(3);

        // when
        Integer waitQna = qnaQueryService.getWaitQna();

        // then
        verify(qnaRepository).countAllByAnswerIsNull();
        assertThat(waitQna).isEqualTo(3);
    }
}
