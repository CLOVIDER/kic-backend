package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.dto.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest;
import clovider.clovider_be.domain.qna.dto.QnaUpdateResponse;
import org.springframework.stereotype.Service;

@Service
public interface QnaCommandService {

    CustomResult createQna(Employee employee, QnaRequest qnaRequest);

    QnaUpdateResponse updateQna(Long qnaId, QnaRequest qnaRequest);

    String deleteQna(Long qnaId);

    QnaUpdateResponse updateAnswer(Employee admin, Long qnaId, QnaAnswerRequest qnaAnswerRequest);
}
