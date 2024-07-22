package clovider.clovider_be.domain.kindergarten.service;

import clovider.clovider_be.domain.common.CustomResult;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KindergartenCommandServiceImpl implements
        clovider.clovider_be.domain.kindergarten.service.KindergartenCommandService {
    private final KindergartenRepository kindergartenRepository;

    @Override
    public CustomResult registerKdg(String kdgName, String kdgAddress, String kdgScale,
            String kdgNo, String kdgTime, String kdgInfo, String kdgImage) {

        Kindergarten kindergarten = Kindergarten.builder()
                .kdgName(Optional.ofNullable(kdgName).orElse("Default Name"))
                .kdgAddress(Optional.ofNullable(kdgAddress).orElse("Default Address"))
                .kdgScale(Optional.ofNullable(kdgScale).orElse("Default Scale"))
                .kdgNo(Optional.ofNullable(kdgNo).orElse("000-0000-0000"))
                .kdgTime(Optional.ofNullable(kdgTime).orElse("00:00 - 00:00"))
                .kdgInfo(Optional.ofNullable(kdgInfo).orElse("Default Info"))
                .build();

        kindergarten = kindergartenRepository.save(kindergarten);

        return CustomResult.toCustomResult(kindergarten.getId());
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
