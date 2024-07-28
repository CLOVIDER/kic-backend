package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomPage;
import clovider.clovider_be.domain.enums.SearchType;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public QnaResponse getQna(Long qnaId) {
        return Qna.toQnaResponse(qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ApiException(ErrorStatus._QNA_NOT_FOUND)));
    }

    @Override
    public CustomPage<QnaResponse> getAllQnas(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Qna> qnaPage = qnaRepository.findAll(pageRequest);

        Page<QnaResponse> qnaResponsePage = qnaPage.map(Qna::toQnaResponse);

        return new CustomPage<>(qnaResponsePage);
    }

    @Override
    public Integer getWaitQna() {
        return qnaRepository.countAllByAnswerIsNull();
    }

    @Override
    public List<QnaResponse> searchQnas(SearchType type, String keyword) {
        return qnaRepository.searchQnas(type, keyword);
    }
}
