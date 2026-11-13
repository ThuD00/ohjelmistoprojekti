package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LipputyyppiServiceTest {
    @Mock
    private LipputyyppiRepository lipputyyppiRepository;

    @Mock
    private TapahtumaRepository tapahtumaRepository;

    @Mock
    private LippuRepository lippuRepository;

    @InjectMocks
    private LipputyyppiService lipputyyppiService;

    private Tapahtuma tapahtuma;

    @BeforeEach
    void setUp() {
        tapahtuma = mock(Tapahtuma.class);
    }

    @Test
    void calculateLiputjaljellaReturnsRemainingTickets() {
        Lipputyyppi lipputyyppi = mock(Lipputyyppi.class);

        when(lipputyyppi.getMaxLippumaara()).thenReturn(50);
        when(lippuRepository.countByLipputyyppiAndLipunStatusNot(
                lipputyyppi, Lippu.LippuTila.PERUTTU))
                .thenReturn(15L);

        assertEquals(35L,
                lipputyyppiService.calculateLiputjaljella(lipputyyppi));
    }

    @Test
    void calculateLiputjaljellaReturnsNullWithoutLimit() {
        Lipputyyppi lipputyyppi = mock(Lipputyyppi.class);
        when(lipputyyppi.getMaxLippumaara()).thenReturn(null);

        assertNull(lipputyyppiService.calculateLiputjaljella(lipputyyppi));
    }

    @Test
    void nextLipputyyppiIdReturnsOneWhenNoTypesExist() {
        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.of(tapahtuma));

        assertEquals(1L, lipputyyppiService.nextLipputyyppiId(1L));
    }

    @Test
    void nextLipputyyppiIdReturnsNextId() {
        Lipputyyppi first = mock(Lipputyyppi.class);
        Lipputyyppi second = mock(Lipputyyppi.class);

        when(second.getId()).thenReturn(new LipputyyppiId(1, 2));

        when(tapahtuma.getLipputyypit())
                .thenReturn(new ArrayList<>(List.of(first, second)));

        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.of(tapahtuma));

        assertEquals(3L, lipputyyppiService.nextLipputyyppiId(1L));
    }
}
