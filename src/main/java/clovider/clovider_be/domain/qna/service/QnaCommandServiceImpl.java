package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.global.auth.service.AuthService;
import clovider.clovider_be.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaCommandServiceImpl implements QnaCommandService {

    private final QnaRepository qnaRepository;
    private final QnaQueryService qnaQueryService;
    private final EmployeeQueryService employeeQueryService;
    private final AuthService authService;

    @Override
    public CustomResult createQna(QnaRequest qnaRequest) {
        Employee employee = authService.getCurrentEmployee();

        // 2번 데이터로 고정
        Qna savedQna = qnaRepository.save(QnaRequest
                .toQna(qnaRequest, employeeQueryService.getEmployee(employee.getId())));

        return CustomResult.toCustomResult(savedQna.getId());
    }

    @Override
    public QnaUpdateResponse updateQna(Long qnaId, QnaRequest qnaRequest) {
        Qna foundQna = qnaQueryService.findById(qnaId);

        foundQna.updateQna(qnaRequest);

        return QnaUpdateResponse.of(qnaId);
    }

    @Override
    public String deleteQna(Long qnaId) {
        qnaRepository.deleteById(qnaId);
        return "qna 삭제에 성공하였습니다.";
    }


}
