package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResisterResponseDTO;
import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LotteryController {

    private final LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/api/admin/lotteries/create/{recruitId}/{applicationId}")
    public ResponseEntity<LotteryResponseDTO> createLottery(
            @PathVariable Long recruitId,
            @PathVariable Long applicationId) {
        try {
            LotteryResponseDTO response = lotteryService.createLottery(recruitId, applicationId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new LotteryResponseDTO(false, "COMMON400", "유효하지 않은 요청입니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LotteryResponseDTO(false, "COMMON500", "서버 내부 오류", null));
        }
    }

    @PatchMapping("/api/update-registry-true/{lotteryId}")
    public ResponseEntity<LotteryResisterResponseDTO> updateRegistryTrue(@PathVariable Long lotteryId) {
        try {
            LotteryResisterResponseDTO response = lotteryService.updateRegistryTrue(lotteryId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new LotteryResisterResponseDTO(false, "COMMON400", "유효하지 않은 요청입니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LotteryResisterResponseDTO(false, "COMMON500", "서버 내부 오류", null));
        }
    }

    @PatchMapping("/api/update-registry-false/{lotteryId}")
    public ResponseEntity<LotteryResisterResponseDTO> updateRegistryFalse(@PathVariable Long lotteryId) {
        try {
            LotteryResisterResponseDTO response = lotteryService.updateRegistryFalse(lotteryId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new LotteryResisterResponseDTO(false, "COMMON400", "유효하지 않은 요청입니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new LotteryResisterResponseDTO(false, "COMMON500", "서버 내부 오류", null));
        }
    }
}
