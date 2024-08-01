package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LotteryController {

    private final LotteryCommandService lotteryService;
    private final LotteryQueryService lotteryQueryService;
    @Autowired
    public LotteryController(LotteryCommandService lotteryService, LotteryQueryService lotteryQueryService) {

        this.lotteryService = lotteryService;
        this.lotteryQueryService = lotteryQueryService;
    }

    @Operation(summary = "추첨 생성 및 진행", description = "관리자가 추첨을 생성함과 동시에 실행한다. 모집의 가중치 사용여부에 따라서 진행된다.")
    @Parameter(name = "recruitId", description = "모집ID")
    @PostMapping("/admin/lotteries/{recruitId}")
    public ApiResponse<LotteryResponseDTO> createLottery(
            @PathVariable Long recruitId) {
        return ApiResponse.onSuccess(lotteryService.createLottery(recruitId));
    }

    @Operation(summary = "어린이집 등록", description = "추첨에 당첨된 사용자가 어린이집을 등록할 것인지 한번더 확인하는 API")
    @Parameter(name = "lotteryId", description = "추첨ID")
    @PatchMapping("/update/registry/{lotteryId}")
    public ApiResponse<LotteryResisterResponseDTO> updateRegistry(@PathVariable Long lotteryId) {
        return  ApiResponse.onSuccess(lotteryService.updateRegistry(lotteryId));
    }

    @Operation(summary = "추첨 결과 조회", description = "추첨ID에 따라서 추첨 결과를 조회한다.")
    @Parameter(name = "lotteryId", description = "추첨ID")
    @GetMapping("/lotteries/{lotteryId}")
    public ApiResponse<LotteryResultResponseDTO> getLottery(@PathVariable Long lotteryId) {
        return ApiResponse.onSuccess(lotteryQueryService.getLotteryResult(lotteryId));
    }

    @GetMapping("/recruits/{lotteryId}/percents")
    public ApiResponse<Double> getPercentage(@PathVariable Long lotteryId) {
        return ApiResponse.onSuccess(lotteryService.getPercent(lotteryId));
    }

    @Operation(summary = "추첨 취소 ", description = "추첨 신청을 취소한다.")
    @Parameter(name = "lotteryId", description = "추첨ID")
    @DeleteMapping("/{lotteryId}")
    public ApiResponse<String> deleteLottery(@PathVariable Long lotteryId) {
        lotteryService.deleteLotteryBylotteryId(lotteryId);
        return ApiResponse.onSuccess("추첨이 삭제되었습니다.");
    }


}
