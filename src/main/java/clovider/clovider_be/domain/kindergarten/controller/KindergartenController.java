package clovider.clovider_be.domain.kindergarten.controller;

import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenDeleteResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenUpdateResponse;
import clovider.clovider_be.domain.kindergarten.service.KindergartenCommandService;
import clovider.clovider_be.domain.kindergarten.service.KindergartenQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "어린이집 관련 API 명세서", description = "어린이집 CRUD관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KindergartenController {
    private final KindergartenCommandService kindergartenCommandService;
    private final KindergartenQueryService kindergartenQueryService;

    @Operation(summary = "어린이집 정보 등록", description = "새로운 어린이집 상세정보를 등록하는 API입니다. \n 관리자용 API입니다.")
    @PostMapping("/admin/kindergartens")
    public ApiResponse<KindergartenRegisterResponse> registerKindergarten(
            @Parameter(description = "어린이집 이름", required = true, example = "샛별 어린이집")
            @RequestParam String kindergartenNm,
            @Parameter(description = "어린이집 주소", required = true, example = "경기도 성남시")
            @RequestParam String kindergartenAddr,
            @Parameter(description = "어린이집 규모(평수)", required = true, example = "500")
            @RequestParam Integer kindergartenScale,
            @Parameter(description = "어린이집 정원(명수)", required = true, example = "100")
            @RequestParam Integer kindergartenCapacity,
            @Parameter(description = "어린이집 전화번호", required = true, example = "031-1234-5678")
            @RequestParam String kindergartenNo,
            @Parameter(description = "어린이집 운영시간", required = true, example = "7:00 - 22:00")
            @RequestParam String kindergartenTime,
            @Parameter(description = "어린이집 기타 정보", required = true, example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.")
            @RequestParam String kindergartenInfo,
            @Parameter(description = "어린이집 이미지 URL 리스트", required = true, example = "[\"path/file1.png\", \"path/file2.png\"]")
            @RequestParam List<String> kindergartenImages) {

        KindergartenRegisterRequest request = KindergartenRegisterRequest.builder()
                .kindergartenNm(kindergartenNm)
                .kindergartenAddr(kindergartenAddr)
                .kindergartenScale(kindergartenScale)
                .kindergartenCapacity(kindergartenCapacity)
                .kindergartenNo(kindergartenNo)
                .kindergartenTime(kindergartenTime)
                .kindergartenInfo(kindergartenInfo)
                .kindergartenImages(kindergartenImages)
                .build();

        KindergartenRegisterResponse result = kindergartenCommandService.registerKindergarten(request);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "어린이집 정보 삭제", description = "어린이집 상세 정보를 삭제하는 API입니다. \n 관리자용 API입니다.")
    @Parameter(name = "kindergartenId", description = "어린이집 ID", required = true, example = "1")
    @DeleteMapping("/admin/kindergartens/{kindergartenId}")
    public ApiResponse<KindergartenDeleteResponse> deleteKindergarten(@PathVariable("kindergartenId") Long kindergartenId) {

        KindergartenDeleteResponse result = kindergartenCommandService.deleteKindergarten(kindergartenId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "어린이집 정보 일부 수정", description = "어린이집 정보를 일부 수정하는 API입니다. \n 관리자용 API입니다.")
    @Parameter(name = "kindergartenId", description = "어린이집 ID", required = true, example = "1")
    @PatchMapping("/admin/kindergartens/{kindergartenId}")
    public ApiResponse<KindergartenUpdateResponse> updateKindergarten(
            @Parameter(description = "어린이집 이름", required = true, example = "샛별 어린이집")
            @RequestParam String kindergartenNm,
            @Parameter(description = "어린이집 주소", required = true, example = "경기도 성남시")
            @RequestParam String kindergartenAddr,
            @Parameter(description = "어린이집 규모(평수)", required = true, example = "500")
            @RequestParam Integer kindergartenScale,
            @Parameter(description = "어린이집 정원(명수)", required = true, example = "100")
            @RequestParam Integer kindergartenCapacity,
            @Parameter(description = "어린이집 전화번호", required = true, example = "031-1234-5678")
            @RequestParam String kindergartenNo,
            @Parameter(description = "어린이집 운영시간", required = true, example = "7:00 - 22:00")
            @RequestParam String kindergartenTime,
            @Parameter(description = "어린이집 기타 정보", required = true, example = "- 저희 어린이집은 어쩌구이고\n- 어쩌구입니다.")
            @RequestParam String kindergartenInfo,
            @Parameter(description = "어린이집 이미지 URL 리스트", required = true, example = "[\"path/file1.png\", \"path/file2.png\"]")
            @RequestParam List<String> kindergartenImage,
            @PathVariable("kindergartenId") Long kindergartenId) {

        KindergartenUpdateRequest request = KindergartenUpdateRequest.builder()
                .kindergartenNm(kindergartenNm)
                .kindergartenAddr(kindergartenAddr)
                .kindergartenScale(kindergartenScale)
                .kindergartenCapacity(kindergartenCapacity)
                .kindergartenNo(kindergartenNo)
                .kindergartenTime(kindergartenTime)
                .kindergartenInfo(kindergartenInfo)
                .kindergartenImages(kindergartenImage)
                .build();

        KindergartenUpdateResponse result = kindergartenCommandService.updateKindergarten(kindergartenId, request);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "개별 어린이집 정보 불러오기", description = "어린이집 개별 정보를 불러오는 API입니다.")
    @Parameter(name = "kindergartenId", description = "어린이집 ID", required = true, example = "1")
    @GetMapping("/kindergartens/{kindergartenId}")
    public ApiResponse<KindergartenGetResponse> getKindergartenInfo(
            @PathVariable("kindergartenId") Long kindergartenId) {

        KindergartenGetResponse result = kindergartenQueryService.getKindergarten(kindergartenId);

        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "전체 어린이집 정보 불러오기", description = "전체 어린이집 정보를 불러오는 API입니다.")
    @GetMapping("/kindergartens")
    public ApiResponse<List<KindergartenGetResponse>> getAllKindergartenInfo() {

        List<KindergartenGetResponse> result = kindergartenQueryService.getAllKindergartens();

        return ApiResponse.onSuccess(result);
    }
}
