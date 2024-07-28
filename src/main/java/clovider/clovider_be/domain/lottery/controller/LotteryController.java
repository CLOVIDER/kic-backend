package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.global.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


}
