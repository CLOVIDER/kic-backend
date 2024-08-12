package clovider.clovider_be.domain.kindergarten.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenGetResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageQueryService;
import clovider.clovider_be.global.exception.ApiException;
import clovider.clovider_be.global.response.code.status.ErrorStatus;
import java.util.ArrayList;
import java.util.Collections;
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
        assertEquals(kindergarten.getKindergartenAddr(), result.getKindergartenAddr());
        assertEquals(kindergarten.getKindergartenScale(), result.getKindergartenScale());
        assertEquals(kindergarten.getKindergartenCapacity(), result.getKindergartenCapacity());
        assertEquals(kindergarten.getKindergartenNo(), result.getKindergartenNo());
        assertEquals(kindergarten.getKindergartenTime(), result.getKindergartenTime());
        assertEquals(kindergarten.getKindergartenInfo(), result.getKindergartenInfo());
        assertEquals(imageUrls, result.getKindergartenImageUrls());
        verify(kindergartenRepository).findById(1L);
        verify(kindergartenImageQueryService).getKindergartenImageUrls(1L);
        verifyNoMoreInteractions(kindergartenRepository, kindergartenImageQueryService);
    }

    @Test
    @DisplayName("Kindergarten 조회 시 예외 발생 테스트")
    void getKindergartenThrowsException() {
        // given
        when(kindergartenRepository.findById(1L)).thenReturn(Optional.empty());

        // when + then
        ApiException exception = assertThrows(ApiException.class, () -> kindergartenQueryService.getKindergarten(1L));
        assertEquals(ErrorStatus._KDG_NOT_FOUND, exception.getErrorStatus());
        verify(kindergartenRepository).findById(1L);
        verifyNoMoreInteractions(kindergartenRepository);
    }

    @Test
    @DisplayName("전체 Kindergarten 조회 테스트")
    void getAllKindergartens() {
        // given
        List<Kindergarten> kindergartens = List.of(kindergarten);
        List<String> imageUrls = List.of("https://kidsincompany-bucket.s3.ap-northeast-2.amazonaws.com/images/kindergarten/0eeeef2b-c1a3-45c6-9f9c-6ccd7350d4de.jpeg");
        when(kindergartenRepository.findAll()).thenReturn(kindergartens);
        when(kindergartenImageQueryService.getKindergartenImageUrls(1L)).thenReturn(imageUrls);

        // when
        List<KindergartenGetResponse> result = kindergartenQueryService.getAllKindergartens();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(kindergarten.getId(), result.get(0).getKindergartenId());
        assertEquals(kindergarten.getKindergartenNm(), result.get(0).getKindergartenNm());
        assertEquals(imageUrls, result.get(0).getKindergartenImageUrls());
        verify(kindergartenRepository).findAll();
        verify(kindergartenImageQueryService).getKindergartenImageUrls(1L);
        verifyNoMoreInteractions(kindergartenRepository, kindergartenImageQueryService);
    }

    @Test
    @DisplayName("전체 Kindergarten 조회 시 빈 리스트 반환 테스트")
    void getAllKindergartensReturnsEmptyList() {
        // given
        when(kindergartenRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<KindergartenGetResponse> result = kindergartenQueryService.getAllKindergartens();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(kindergartenRepository).findAll();
        verifyNoMoreInteractions(kindergartenRepository);
    }

    @Test
    @DisplayName("Kindergarten 기본 정보 조회 테스트")
    void getKindergartenOnly() {
        // given
        when(kindergartenRepository.findById(1L)).thenReturn(Optional.of(kindergarten));

        // when
        Kindergarten result = kindergartenQueryService.getKindergartenOnly(1L);

        // then
        assertNotNull(result);
        assertEquals(kindergarten.getId(), result.getId());
        assertEquals(kindergarten.getKindergartenNm(), result.getKindergartenNm());
        assertEquals(kindergarten.getKindergartenAddr(), result.getKindergartenAddr());
        assertEquals(kindergarten.getKindergartenScale(), result.getKindergartenScale());
        assertEquals(kindergarten.getKindergartenCapacity(), result.getKindergartenCapacity());
        assertEquals(kindergarten.getKindergartenNo(), result.getKindergartenNo());
        assertEquals(kindergarten.getKindergartenTime(), result.getKindergartenTime());
        assertEquals(kindergarten.getKindergartenInfo(), result.getKindergartenInfo());
        verify(kindergartenRepository).findById(1L);
        verifyNoMoreInteractions(kindergartenRepository);
    }

    @Test
    @DisplayName("Kindergarten 기본 정보 조회 시 예외 발생 테스트")
    void getKindergartenOnlyThrowsException() {
        // given
        when(kindergartenRepository.findById(1L)).thenReturn(Optional.empty());

        // when + then
        ApiException exception = assertThrows(ApiException.class, () -> kindergartenQueryService.getKindergartenOnly(1L));
        assertEquals(ErrorStatus._KDG_NOT_FOUND, exception.getErrorStatus());
        verify(kindergartenRepository).findById(1L);
        verifyNoMoreInteractions(kindergartenRepository);
    }
}
