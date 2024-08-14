package clovider.clovider_be.domain.recruit.service;

import clovider.clovider_be.domain.admin.dto.AdminRequest.RecruitCreationRequest;
import java.util.List;

public interface RecruitCommandService {

    List<Long> resetKindergarten(Long kindergartenId);

    String updateRecruit(RecruitCreationRequest request, Long recruitId);

    String createRecruit(RecruitCreationRequest request);
}
