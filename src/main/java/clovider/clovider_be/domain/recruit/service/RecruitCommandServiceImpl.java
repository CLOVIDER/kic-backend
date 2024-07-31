package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.recruit.Recruit;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateResponseDTO;
import clovider.clovider_be.domain.recruit.repository.RecruitRepository;
import java.util.ArrayList;
import java.util.List;

import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitCommandServiceImpl implements RecruitCommandService{
    private final RecruitQueryService recruitQueryService;
    private final RecruitRepository recruitRepository;
    private final KindergartenRepository kindergartenRepository;

    @Override
    public List<Long> resetKindergarten(Long kindergartenId) {
        List<Recruit> recruits = recruitQueryService.getRecruitByKindergarten(kindergartenId);
        List<Long> recruitIds = new ArrayList<>();

        for (Recruit recruit : recruits) {
            recruit.changeKindergarten(null);
            recruitRepository.save(recruit);
            recruitIds.add(recruit.getId());
        }

        return recruitIds;
    }

    @Transactional
    public RecruitCreateResponseDTO createRecruit(RecruitCreateRequestDTO requestDTO) {
        Kindergarten kindergarten = kindergartenRepository.findById(requestDTO.getKindergartenId())
                .orElseThrow(() -> new ApiException(ErrorStatus._KDG_NOT_FOUND));

        Recruit recruit = Recruit.createRecruit(requestDTO, kindergarten);

        Recruit savedRecruit = recruitRepository.save(recruit);

        return new RecruitCreateResponseDTO(savedRecruit.getId(), savedRecruit.getCreatedAt(), savedRecruit.getUpdatedAt());
    }





}
