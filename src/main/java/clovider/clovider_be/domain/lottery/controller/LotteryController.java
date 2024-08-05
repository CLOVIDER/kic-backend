package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.ChildInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LotteryController {

    private final LotteryCommandService lotteryService;
    private final LotteryQueryService lotteryQueryService;

    @PostMapping("/admin/lotteries/create/{recruitId}/{applicationId}")
    public ApiResponse<LotteryResponseDTO> createLottery(
            @PathVariable Long recruitId,
            @PathVariable Long applicationId) {
        return ApiResponse.onSuccess(lotteryService.createLottery(recruitId, applicationId));
    }

    @PatchMapping("/update-registry/{lotteryId}")
    public ApiResponse<LotteryResisterResponseDTO> updateRegistry(@PathVariable Long lotteryId) {
        return  ApiResponse.onSuccess(lotteryService.updateRegistry(lotteryId));
    }
    @GetMapping("/lotteries/{lotteryId}")
    public ApiResponse<LotteryResultResponseDTO> getLottery(@PathVariable Long lotteryId) {
        return ApiResponse.onSuccess(lotteryQueryService.getLotteryResult(lotteryId));
    }

    @GetMapping("/recruits/{lotteryId}/percents")
    public ApiResponse<Double> getPercentage(@PathVariable Long lotteryId) {
        return ApiResponse.onSuccess(lotteryService.getPercent(lotteryId));
    }

    @DeleteMapping("/delete/{lotteryId}")
    public ApiResponse<String> deleteLottery(@PathVariable Long lotteryId) {
        lotteryService.deleteLotteryBylotteryId(lotteryId);
        return ApiResponse.onSuccess("추첨이 삭제되었습니다.");
    }

    @Operation(summary = "아이 리스트 정보 조회 - 신청서 조회 페이지, 관리자 신청 승인 페이지", description = "특정 신청서에 제출된 아이 리스트 정보를 조회합니다.")
    @Parameter(name = "applicationId", description = "신청서 ID", required = true, example = "1")
    @GetMapping("/lotteries/children/{applicationId}")
    public ApiResponse<List<ChildInfo>> getChildrenInfos(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(lotteryQueryService.getChildInfos(applicationId));
    }

}
