package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.repository.EmployeeRepository;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaCommandServiceImpl implements QnaCommandService {

    private final QnaRepository qnaRepository;
    private final EmployeeRepository employeeRepository;

    private static final String FIXED_EMPLOYEE_USERNAME = "employee";

    public CustomResult createQna(QnaRequest qnaRequest) {
        Employee employee = employeeRepository.findByAccountId(FIXED_EMPLOYEE_USERNAME)
                .orElseThrow(() -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND));

        Qna savedQna = qnaRepository.save(Qna.builder()
                .title(qnaRequest.getTitle())
                .question(qnaRequest.getQuestion())
                .visibility(qnaRequest.isVisibility())
                .employee(employee).build());

        return CustomResult.toCustomResult(savedQna.getId());
    }
}
