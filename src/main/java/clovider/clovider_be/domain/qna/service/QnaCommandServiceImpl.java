package clovider.clovider_be.domain.qna.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.employee.service.EmployeeQueryService;
import clovider.clovider_be.domain.qna.Qna;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaAnswerRequest;
import clovider.clovider_be.domain.qna.dto.QnaRequest.QnaCreateRequest;
import clovider.clovider_be.domain.qna.dto.QnaResponse.QnaUpdateResponse;
import clovider.clovider_be.domain.qna.repository.QnaRepository;
import clovider.clovider_be.domain.qnaImage.QnaImage;
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

    @Override
    public CustomResult createQna(Employee employee, QnaCreateRequest qnaCreateRequest) {

        Qna savedQna = qnaRepository.save(QnaCreateRequest
                .toQna(qnaCreateRequest, employee));

        qnaCreateRequest.getImageUrls().forEach(url -> {
            QnaImage image = QnaImage.builder()
                    .image(url)
                    .qna(savedQna)
                    .build();
            savedQna.getImages().add(image);
        });

        return CustomResult.toCustomResult(savedQna.getId());
    }

    @Override
    public QnaUpdateResponse updateQna(Long qnaId, QnaCreateRequest qnaCreateRequest) {
        Qna foundQna = qnaQueryService.findById(qnaId);

        foundQna.updateQna(qnaCreateRequest);

        return QnaUpdateResponse.of(qnaId);
    }

    @Override
    public String deleteQna(Long qnaId) {
        qnaRepository.deleteById(qnaId);
        return "qna 삭제에 성공하였습니다.";
    }

    @Override
    public QnaUpdateResponse updateAnswer(Employee admin, Long qnaId, QnaAnswerRequest qnaAnswerRequest) {
        Qna foundQna = qnaQueryService.findById(qnaId);

        foundQna.updateAnswer(qnaAnswerRequest.getAnswer(), admin);

        return QnaUpdateResponse.of(qnaId);
    }
    
}
