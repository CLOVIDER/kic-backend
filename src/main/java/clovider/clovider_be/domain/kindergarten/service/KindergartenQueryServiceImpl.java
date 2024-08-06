package clovider.clovider_be.domain.kindergarten.service;


import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.domain.recruit.service.RecruitQueryService;
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
    private final RecruitQueryService recruitQueryService;

    @Override
    public KindergartenGetResponse getKindergarten(Long kindergartenId) {
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        String imageUrls = kindergartenImageQuery.getKindergartenImageUrls(kindergartenId);

        return KindergartenGetResponse.toKindergartenGetResponse(kindergarten, imageUrls);
    }

    @Override
    public List<KindergartenGetResponse> getAllKindergartens() {
        List<Kindergarten> kindergartens = kindergartenRepository.findAll();
        List<KindergartenGetResponse> responses = new ArrayList<>();

        List<String> imageUrlsList = kindergartens.stream()
                .map(kindergarten -> kindergartenImageQuery.getKindergartenImageUrls(
                        kindergarten.getId()))
                .collect(Collectors.toList());

        for (int i = 0; i < kindergartens.size(); i++) {
            Kindergarten kindergarten = kindergartens.get(i);
            String imageUrls = imageUrlsList.get(i);
            responses.add(
                    KindergartenGetResponse.toKindergartenGetResponse(kindergarten, imageUrls));
        }

        return responses;
    }

}
