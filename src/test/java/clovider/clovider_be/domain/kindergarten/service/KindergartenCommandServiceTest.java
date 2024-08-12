package clovider.clovider_be.domain.kindergarten.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import clovider.clovider_be.domain.kindergarten.Kindergarten;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenRegisterRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenRequest.KindergartenUpdateRequest;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenDeleteResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenRegisterResponse;
import clovider.clovider_be.domain.kindergarten.dto.KindergartenResponse.KindergartenUpdateResponse;
import clovider.clovider_be.domain.kindergarten.repository.KindergartenRepository;
import clovider.clovider_be.domain.kindergartenImage.service.KindergartenImageCommandService;
import clovider.clovider_be.domain.recruit.service.RecruitCommandService;
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
class KindergartenCommandServiceTest {

    @InjectMocks
    private KindergartenCommandServiceImpl kindergartenCommandService;

    @Mock
    private KindergartenRepository kindergartenRepository;

    @Mock
    private KindergartenImageCommandService kindergartenImageCommandService;

    @Mock
    private RecruitCommandService recruitCommandService;

    private Kindergarten kindergarten;
    private KindergartenRegisterRequest registerRequest;
    private KindergartenUpdateRequest updateRequest;

    @BeforeEach
    void setup() {
        kindergarten = Kindergarten.builder()
                .id(1L)
                .kindergartenNm("애옹유치원")
                .kindergartenAddr("경기도 성남시")
                .kindergartenScale(50)
                .kindergartenCapacity(100)
                .kindergartenNo("031-1234-5678")
                .kindergartenTime("08:00 - 18:00")
                .kindergartenInfo("애옹이가 다니는 유치원")
                .build();

        registerRequest = KindergartenRegisterRequest.builder()
                .kindergartenNm("애옹유치원")
                .kindergartenAddr("경기도 성남시")
                .kindergartenScale(50)
                .kindergartenCapacity(100)
                .kindergartenNo("031-1234-5678")
                .kindergartenTime("08:00 - 18:00")
                .kindergartenInfo("애옹이가 다니는 유치원")
                .kindergartenImages(new ArrayList<>())
                .build();

        updateRequest = KindergartenUpdateRequest.builder()
                .kindergartenNm("주애유치원")
                .kindergartenAddr("경기도 안양시")
                .kindergartenScale(50)
                .kindergartenCapacity(100)
                .kindergartenNo("031-1234-5678")
                .kindergartenTime("08:00 - 18:00")
                .kindergartenInfo("주애가 다니는 유치원")
                .kindergartenImages(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Kindergarten 등록 테스트")
    void registerKindergarten() {
        // given
        Kindergarten savedKindergarten = kindergarten.toBuilder().build();
        List<Long> imageIds = List.of(1L);
        when(kindergartenRepository.save(any(Kindergarten.class))).thenReturn(savedKindergarten);
        when(kindergartenImageCommandService.saveKindergartenImage(any(Kindergarten.class), anyList())).thenReturn(imageIds);

        // when
        KindergartenRegisterResponse result = kindergartenCommandService.registerKindergarten(registerRequest);

        // then
        assertNotNull(result);
        assertEquals(savedKindergarten.getId(), result.getKindergartenId());
        assertEquals(imageIds, result.getKindergartenImageIds());
        verify(kindergartenRepository).save(any(Kindergarten.class));
        verify(kindergartenImageCommandService).saveKindergartenImage(any(Kindergarten.class), anyList());
        verifyNoMoreInteractions(kindergartenRepository, kindergartenImageCommandService);
    }

    @Test
    @DisplayName("Kindergarten 삭제 테스트")
    void deleteKindergarten() {
        // given
        List<Long> recruitIds = List.of(1L, 2L);
        when(kindergartenRepository.findById(anyLong())).thenReturn(Optional.of(kindergarten));
        when(recruitCommandService.resetKindergarten(anyLong())).thenReturn(recruitIds);

        // when
        KindergartenDeleteResponse result = kindergartenCommandService.deleteKindergarten(1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getKindergartenId());
        assertEquals(recruitIds, result.getRelatedRecruit());
        verify(kindergartenRepository).findById(anyLong());
        verify(recruitCommandService).resetKindergarten(anyLong());
        verify(kindergartenRepository).deleteById(anyLong());
        verifyNoMoreInteractions(kindergartenRepository, recruitCommandService);
    }

    @Test
    @DisplayName("Kindergarten 삭제 시 예외 발생 테스트")
    void deleteKindergartenThrowsException() {
        // given
        when(kindergartenRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when + then
        assertThrows(ApiException.class, () -> kindergartenCommandService.deleteKindergarten(1L));
        verify(kindergartenRepository).findById(anyLong());
        verifyNoMoreInteractions(kindergartenRepository);
    }

    @Test
    @DisplayName("Kindergarten 업데이트 테스트")
    void updateKindergarten() {
        // given
        Kindergarten updatedKindergarten = kindergarten.toBuilder().kindergartenNm("업데이트유치원").build();
        List<Long> imageIds = List.of(1L);
        when(kindergartenRepository.findById(anyLong())).thenReturn(Optional.of(kindergarten));
        when(kindergartenRepository.save(any(Kindergarten.class))).thenReturn(updatedKindergarten);
        when(kindergartenImageCommandService.updateKindergartenImage(any(Kindergarten.class), anyList())).thenReturn(imageIds);

        // when
        KindergartenUpdateResponse result = kindergartenCommandService.updateKindergarten(1L, updateRequest);

        // then
        assertNotNull(result);
        assertEquals(updatedKindergarten.getId(), result.getKindergartenId());
        assertEquals(imageIds, result.getKindergartenImageIds());
        verify(kindergartenRepository).findById(anyLong());
        verify(kindergartenRepository).save(any(Kindergarten.class));
        verify(kindergartenImageCommandService).updateKindergartenImage(any(Kindergarten.class), anyList());
        verifyNoMoreInteractions(kindergartenRepository, kindergartenImageCommandService);
    }

    @Test
    @DisplayName("Kindergarten 업데이트 시 예외 발생 테스트")
    void updateKindergartenThrowsException() {
        // given
        when(kindergartenRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when + then
        assertThrows(ApiException.class, () -> kindergartenCommandService.updateKindergarten(1L, updateRequest));
        verify(kindergartenRepository).findById(anyLong());
        verifyNoMoreInteractions(kindergartenRepository);
    }
}
