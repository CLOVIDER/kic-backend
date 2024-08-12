package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.enums.Role;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.BaseQnaResponse.DetailedQnaResponse;
import clovider.clovider_be.domain.qna.dto.QnaResponse.QnaAnswerResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaQueryServiceImpl implements QnaQueryService {

    private final QnaRepository qnaRepository;

    @Override
    public Qna findById(Long id) {
        return qnaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._QNA_NOT_FOUND));
    }

    @Override
    public DetailedQnaResponse getQna(Employee employee, Long qnaId) {
        Qna foundQna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ApiException(ErrorStatus._QNA_NOT_FOUND));

        // ROLE이 직원이고 QNA가 비공개이며 QNA를 작성한 직원이 아닐 때
        if (employee.getRole().equals(Role.EMPLOYEE) &&
                foundQna.getIsVisibility() == '0' &&
                !foundQna.getEmployee().getId().equals(employee.getId())) {
            throw new ApiException(ErrorStatus._QNA_NO_READ_PERMISSION);
        }

        return DetailedQnaResponse.fromQna(foundQna, employee.getId());
    }

    @Override
    public Page<BaseQnaResponse> getAllQnas(Pageable pageable, SearchType type, String keyword) {
        return qnaRepository.searchQnas(pageable, type, keyword);
    }

    @Override
    public Integer getWaitQna() {
        return qnaRepository.countAllByAnswerIsNull();
    }

    @Override
    public QnaAnswerResponse getQnaAnswer(Long qnaId) {
        Qna foundQna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ApiException(ErrorStatus._QNA_NOT_FOUND));
        return QnaAnswerResponse.fromQna(foundQna);
    }
}
