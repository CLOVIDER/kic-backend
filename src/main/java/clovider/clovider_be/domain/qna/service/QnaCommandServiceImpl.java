package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaCommandServiceImpl implements QnaCommandService {

    private final QnaRepository qnaRepository;
    private final QnaQueryService qnaQueryService;
    private final EmployeeQueryService employeeQueryService;

    public CustomResult createQna(QnaRequest qnaRequest) {

        // 2번 데이터로 고정
        Qna savedQna = qnaRepository.save(QnaRequest
                .toQna(qnaRequest, employeeQueryService.getEmployee(2L)));

        return CustomResult.toCustomResult(savedQna.getId());
    }

    @Override
    public CustomResult updateQna(Long qnaId, QnaRequest qnaRequest) {
        Qna foundQna = qnaQueryService.findById(qnaId);

        foundQna.updateQna(qnaRequest);

        return CustomResult.toCustomResult(foundQna.getId());
    }
}
