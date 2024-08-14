package clovider.clovider_be.domain.kindergarten.service;


import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenClass.dto.KindergartenClassDTO;
import clovider.clovider_be.domain.kindergartenClass.service.KindergartenClassQueryService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KindergartenQueryServiceImpl implements KindergartenQueryService {

    private final KindergartenRepository kindergartenRepository;
    private final KindergartenImageQueryService kindergartenImageQuery;
    private final KindergartenClassQueryService kindergartenClassQueryService;

    @Override
    public KindergartenGetResponse getKindergarten(Long kindergartenId) {

        Kindergarten kindergarten = getKindergartenOnly(kindergartenId);

        List<String> imageUrls = kindergartenImageQuery.getKindergartenImageUrls(kindergartenId);

        List<KindergartenClassDTO> kindergartenClasses = kindergartenClassQueryService.getKindergartenClass(
                kindergartenId);

        List<KindergartenClassDTO> newKindergartenClasses = kindergartenClasses.stream()
                .map(KindergartenClassDTO::toKindergartenClassResponse)
                .collect(Collectors.toList());

        return KindergartenGetResponse.toKindergartenGetResponse(kindergarten,
                newKindergartenClasses, imageUrls);
    }

    @Override
    public Kindergarten getKindergartenOnly(Long kindergartenId) {
        return kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));
    }


    @Override
    public List<KindergartenGetResponse> getAllKindergartens() {

        List<Kindergarten> kindergartens = kindergartenRepository.findAll();

        List<KindergartenGetResponse> responses = new ArrayList<>();

        for (Kindergarten kindergarten : kindergartens) {
            List<String> imageUrls = kindergartenImageQuery.getKindergartenImageUrls(
                    kindergarten.getId());

            List<KindergartenClassDTO> kindergartenClasses = kindergartenClassQueryService.getKindergartenClass(
                    kindergarten.getId());

            List<KindergartenClassDTO> newKindergartenClasses = kindergartenClasses.stream()
                    .map(KindergartenClassDTO::toKindergartenClassResponse)
                    .collect(Collectors.toList());

            KindergartenGetResponse response = KindergartenGetResponse.toKindergartenGetResponse(
                    kindergarten, newKindergartenClasses, imageUrls);

            responses.add(response);
        }

        return responses;
    }

    @Override
    public Kindergarten getKindergartenByName(String kindergartenName) {
        return kindergartenRepository.findByKindergartenNm(kindergartenName);
    }

}
