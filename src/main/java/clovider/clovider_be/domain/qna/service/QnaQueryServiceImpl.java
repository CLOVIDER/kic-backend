package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaQueryServiceImpl implements QnaQueryService {

    private final QnaRepository qnaRepository;

    @Override
    public Qna findById(Long id) {
        return qnaRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._QNA_NOT_FOUND));
    }
}
