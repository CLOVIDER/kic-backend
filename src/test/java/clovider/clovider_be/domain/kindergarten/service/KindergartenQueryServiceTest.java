package clovider.clovider_be.domain.kindergarten.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.global.exception.ApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KindergartenQueryServiceTest {
    @InjectMocks
    private KindergartenQueryServiceImpl kindergartenQueryService;

    @Mock
    private KindergartenRepository kindergartenRepository;

    @Mock
    private KindergartenImageQueryService kindergartenImageQueryService;

    private Kindergarten kindergarten;

    @BeforeEach
    void setup() {
        kindergarten = Kindergarten.builder()
                .id(1L)
                .kindergartenNm("샛별 어린이집")
                .kindergartenAddr("경기도 성남시")
                .kindergartenScale(500)
                .kindergartenCapacity(100)
                .kindergartenNo("031-1234-5678")
                .kindergartenTime("7:00 - 22:00")
                .kindergartenInfo("- 저희 어린이집은 어쩌구이고\\n- 어쩌구입니다.")
                .kindergartenImages(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Kindergarten 조회 테스트")
    void getKindergarten() {
        // given
        List<String> imageUrls = List.of("https://kidsincompany-bucket.s3.ap-northeast-2.amazonaws.com/images/kindergarten/0eeeef2b-c1a3-45c6-9f9c-6ccd7350d4de.jpeg");
        when(kindergartenRepository.findById(1L)).thenReturn(Optional.of(kindergarten));
        when(kindergartenImageQueryService.getKindergartenImageUrls(1L)).thenReturn(imageUrls);

        // when
        KindergartenGetResponse result = kindergartenQueryService.getKindergarten(1L);

        // then
        assertNotNull(result);
        assertEquals(kindergarten.getId(), result.getKindergartenId());
        assertEquals(kindergarten.getKindergartenNm(), result.getKindergartenNm());
        assertEquals(imageUrls, result.getKindergartenImageUrls());
        verify(kindergartenRepository).findById(1L);
        verify(kindergartenImageQueryService).getKindergartenImageUrls(1L);
    }

    @Test
    @DisplayName("Kindergarten 조회 시 예외 발생 테스트")
    void getKindergartenThrowsException() {
        // given
        when(kindergartenRepository.findById(1L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(ApiException.class, () -> kindergartenQueryService.getKindergarten(1L));
    }
}
