package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandServiceImpl;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class KindergartenCommandServiceImpl implements KindergartenCommandService {
    private final KindergartenRepository kindergartenRepository;
    private final KindergartenImageCommandService kindergartenImageCommandService;

    @Override
    public KindergartenResponse registerKdg(String kindergartenNm, String kindergartenAddr, String kindergartenScale,
            String kindergartenNo, String kindergartenTime, String kindergartenInfo, MultipartFile kindergartenImage) {

        Kindergarten kindergarten = Kindergarten.builder()
                .kindergartenNm(Optional.ofNullable(kindergartenNm).orElse("Default Name"))
                .kindergartenAddr(Optional.ofNullable(kindergartenAddr).orElse("Default Address"))
                .kindergartenScale(Optional.ofNullable(kindergartenScale).orElse("Default Scale"))
                .kindergartenNo(Optional.ofNullable(kindergartenNo).orElse("000-0000-0000"))
                .kindergartenTime(Optional.ofNullable(kindergartenTime).orElse("00:00 - 00:00"))
                .kindergartenInfo(Optional.ofNullable(kindergartenInfo).orElse("Default Info"))
                .build();

        kindergarten = kindergartenRepository.save(kindergarten);

        Long kindergartenImageId = kindergartenImageCommandService.saveKindergartenImage(kindergarten, kindergartenImage);

        return KindergartenResponse.toKindergertenResponse(kindergarten.getId(), kindergartenImageId);
    }

    @Override
    public CustomResult deleteKdg(Long kdgId) {

        Optional<Kindergarten> kindergarten = kindergartenRepository.findById(kdgId);

        if (kindergarten.isEmpty()) {
            throw new ApiException(ErrorStatus._KDG_NOT_FOUND);
        }

        kindergartenRepository.deleteById(kdgId);

        return CustomResult.toCustomResult(kdgId);
    }
}
