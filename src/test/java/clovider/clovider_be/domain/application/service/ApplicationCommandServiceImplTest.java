package clovider.clovider_be.domain.application.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ApplicationCommandServiceImplTest {

    @Test
    @DisplayName(value = "신청서 작성 테스트")
    void applicationCreate() {
    }

    @Test
    @DisplayName(value = "신청서 수정 테스트")
    void applicationUpdate() {
    }

    @Test
    @DisplayName(value = "신청서 삭제 테스트")
    void applicationDelete() {
    }

    @Test
    @DisplayName(value = "신청서 임시저장 테스트")
    void applicationTempSave() {
    }

    @Test
    @DisplayName(value = "관리자 신청서 승인 테스트")
    void applicationAccept() {
    }
}