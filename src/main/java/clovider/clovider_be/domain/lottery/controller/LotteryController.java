package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.application.service.ApplicationQueryService;
import clovider.clovider_be.domain.employee.Employee;
import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponse.ChildInfo;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResultResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryCommandService;
import clovider.clovider_be.domain.lottery.service.LotteryQueryService;
import clovider.clovider_be.global.annotation.AuthEmployee;
import clovider.clovider_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    private final ApplicationQueryService applicationQueryService;

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

    @Operation(summary = "아이 리스트 정보 조회 - 신청서 조회 페이지, 관리자 신청 승인 페이지", description = "특정 신청서에 제출된 아이 리스트 정보를 조회합니다.")
    @Parameter(name = "applicationId", description = "신청서 ID", required = true, example = "1")
    @GetMapping("/lotteries/children/{applicationId}")
    public ApiResponse<List<ChildInfo>> getChildrenInfos(@PathVariable Long applicationId) {
        return ApiResponse.onSuccess(lotteryQueryService.getChildInfos(applicationId));
    }

    @Operation(summary = "아이 리스트 정보 조회 - 신청서 조회 페이지", description = "사용자 신청서에 제출된 아이 리스트 정보를 조회합니다.")
    @GetMapping("/lotteries/children")
    public ApiResponse<List<ChildInfo>> getChildrenInfosUsingToken(@AuthEmployee Employee employee) {
        Long applicationId = applicationQueryService.getApplicationId(employee);
        return ApiResponse.onSuccess(lotteryQueryService.getChildInfos(applicationId));
    }
}
