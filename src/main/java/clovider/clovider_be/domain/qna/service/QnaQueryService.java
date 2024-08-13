package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.QnaAnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface QnaQueryService {

    Qna findById(Long id);

    DetailedQnaResponse getQna(Employee employee, Long qnaId);

    Page<BaseQnaResponse> getAllQnas(Pageable pageable, SearchType type, String keyword);

    Integer getWaitQna();

    QnaAnswerResponse getQnaAnswer(Long qnaId);
}
