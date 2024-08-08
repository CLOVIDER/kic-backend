package clovider.clovider_be.domain.qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.global.config.QuerydslConfig;
import java.lang.reflect.Field;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(QuerydslConfig.class)
class QnaRepositoryTest {
    @Autowired
    private QnaRepository qnaRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee admin;
    private Employee employee;

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

        employee = Employee.builder()
                .nameKo("직원1")
                .accountId("admin1")
                .password("admin2")
                .employeeNo("E002")
                .joinDt(LocalDate.of(2023, 1, 1))
                .dept("IT")
                .role(Role.EMPLOYEE)
                .build();

        employeeRepository.save(admin);
        employeeRepository.save(employee);
    }

    private Qna createAndSaveQna(){
        Qna qna = Qna.builder()
                .title("문제입니다")
                .answer(null)
                .question("문제가 있어요")
                .employee(employee)
                .isVisibility('1')
                .build();
        qnaRepository.save(qna);
        return qna;
    }

    private void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Make the field accessible
        field.set(obj, value);
    }

    @Test
    @DisplayName("QNA 생성 및 조회 테스트")
    public void createNoticeTest() {
        // given & when
        Qna savedQna = createAndSaveQna();
        Qna resultQna = qnaRepository.findById(savedQna.getId()).orElse(null);

        // then
        assertNotNull(resultQna);
        assertThat(resultQna.getTitle()).isEqualTo(savedQna.getTitle());
        assertThat(resultQna.getAnswer()).isEqualTo(savedQna.getAnswer());
        assertThat(resultQna.getQuestion()).isEqualTo(savedQna.getQuestion());
        assertThat(resultQna.getEmployee()).isEqualTo(savedQna.getEmployee());
    }

    @Test
    @DisplayName("QNA 수정 테스트")
    public void updateQnaTest() throws NoSuchFieldException, IllegalAccessException {
        // given
        Qna savedQna = createAndSaveQna();

        QnaCreateRequest updateRequest = new QnaCreateRequest();
        setField(updateRequest, "title", "수정된 제목");
        setField(updateRequest, "question", "수정된 문제");
        setField(updateRequest, "isVisibility", '0');

        // when
        savedQna.updateQna(updateRequest);

        // then
        Qna updatedQna = qnaRepository.findById(savedQna.getId()).orElse(null);

        assertNotNull(updatedQna);
        assertThat(updatedQna.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(updatedQna.getQuestion()).isEqualTo(updateRequest.getQuestion());
        assertThat(updatedQna.getIsVisibility()).isEqualTo(updateRequest.getIsVisibility());
    }

    @Test
    @DisplayName("QNA 답변 수정 테스트")
    public void updateAnswerTest() {
        // given
        Qna savedQna = createAndSaveQna();
        String newAnswer = "새로운 답변";

        // when
        savedQna.updateAnswer(newAnswer, admin);

        // then
        Qna updatedQna = qnaRepository.findById(savedQna.getId()).orElse(null);

        assertNotNull(updatedQna);
        assertThat(updatedQna.getAnswer()).isEqualTo(newAnswer);
        assertThat(updatedQna.getAdmin()).isEqualTo(admin);
    }

    @Test
    @DisplayName("QNA 삭제 테스트")
    public void deleteQnaTest(){
        // given
        Qna savedQna = createAndSaveQna();

        // when
        qnaRepository.deleteById(savedQna.getId());

        // then
        assertFalse(qnaRepository.findById(savedQna.getId()).isPresent());
    }

    @Test
    @DisplayName("답변되지 않은 QNA 개수 조회 테스트")
    public void countAllByAnswerTest(){
        // given
        Qna savedQna = createAndSaveQna();
        Qna savedQna2 = createAndSaveQna();
        Qna savedQna3 = createAndSaveQna();

        // when
        Integer cnt = qnaRepository.countAllByAnswerIsNull();

        assertThat(cnt).isEqualTo(3);
    }

    @Test
    @DisplayName("QNA 검색 테스트")
    public void searchQnaTest(){
        createAndSaveQna();
        createAndSaveQna();
        createAndSaveQna();

        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<BaseQnaResponse> qnas = qnaRepository.searchQnas(pageRequest, SearchType.TITLE, "문제");

        // Then
        assertNotNull(qnas);
        assertThat(qnas.getTotalElements()).isEqualTo(3); // 검색 결과의 총 개수 확인
        assertThat(qnas.getTotalPages()).isEqualTo(2); // 페이지 수 확인
        assertThat(qnas.getContent()).hasSize(2); // 현재 페이지의 내용 개수 확인
    }
}