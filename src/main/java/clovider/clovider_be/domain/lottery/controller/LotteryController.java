package clovider.clovider_be.domain.lottery.controller;

import clovider.clovider_be.domain.lottery.dto.LotteryResponseDTO;
import clovider.clovider_be.domain.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lotteries")
public class LotteryController {

    private final LotteryService lotteryService;

    @Autowired
    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @PostMapping("/create/{recruitId}/{applicationId}")
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

    /*
    * 알고리즘 실행 코드
    *
    * */

}
