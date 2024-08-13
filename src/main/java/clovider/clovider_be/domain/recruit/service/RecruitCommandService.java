package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitCreationRequest;
import clovider.clovider_be.domain.admin.dto.AdminResponse;
import clovider.clovider_be.domain.recruit.dto.RecruitCreateRequestDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitResponseDTO;
import clovider.clovider_be.domain.recruit.dto.RecruitUpdateRequestDTO;
import java.util.List;

public interface RecruitCommandService {

    List<Long> resetKindergarten(Long kindergartenId);
    RecruitResponseDTO updateRecruit(RecruitUpdateRequestDTO requestDTO);
    String createRecruit(RecruitCreationRequest request);
}
