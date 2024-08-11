package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateResponseDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitUpdateRequestDTO;

import java.util.List;

public interface RecruitCommandService {

    List<Long> resetKindergarten(Long kindergartenId);
    AdminResponse.RecruitCreationInfo createRecruit(RecruitCreateRequestDTO requestDTO);
    public AdminResponse.RecruitCreationInfo updateRecruit(Long recruitId, RecruitUpdateRequestDTO requestDTO);

}
