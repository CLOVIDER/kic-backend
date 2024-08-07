package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.QnaUpdateResponse;
import org.springframework.stereotype.Service;

@Service
public interface QnaCommandService {

    CustomResult createQna(Employee employee, QnaCreateRequest qnaRequest);

    QnaUpdateResponse updateQna(Long qnaId, QnaCreateRequest qnaCreateRequest);

    String deleteQna(Long qnaId);

    QnaUpdateResponse updateAnswer(Employee admin, Long qnaId, QnaAnswerRequest qnaAnswerRequest);
}
