package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LippuLunastus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVaraus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.dto.LipunTarkastusVastaus;
import ohjelmistoprojekti.lipputoimisto.exception.LippuCapacityException;
import ohjelmistoprojekti.lipputoimisto.exception.LippuStatusException;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LippuServiceTest {

    @Mock
    private LippuRepository lippuRepository;

    @Mock
    private LipputyyppiRepository lipputyyppiRepository;

    @Mock
    private TapahtumaRepository tapahtumaRepository;

    @Mock
    private MyyntitapahtumaRepository myyntitapahtumaRepository;

    @Mock
    private UserContextService userContextService;

    @InjectMocks
    private LippuService lippuService;

    @Test
    void lunastaLippuSuccessfully() {
        Lippu lippu = new Lippu();
        lippu.setKoodi("ABCDEF-123456");
        lippu.setLipunStatus(Lippu.LippuTila.VARATTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        lippuService.lunastaLippu(pyynto);

        assertEquals(
                Lippu.LippuTila.LUNASTETTU,
                lippu.getLipunStatus()
        );

        verify(lippuRepository).save(lippu);
    }

    @Test
    void lunastaLippuNotFound() {
        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("AAAAAA-111111");

        when(lippuRepository.findByKoodi("AAAAAA-111111"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> lippuService.lunastaLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }

    @Test
    void lunastaLippuAlreadyRedeemedThrowsException() {
        Lippu lippu = new Lippu();
        lippu.setLipunStatus(Lippu.LippuTila.LUNASTETTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        assertThrows(
                LippuStatusException.class,
                () -> lippuService.lunastaLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }

    @Test
    void lunastaLippuAlreadyCancelledThrowsException() {
        Lippu lippu = new Lippu();
        lippu.setLipunStatus(Lippu.LippuTila.PERUTTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        assertThrows(
                LippuStatusException.class,
                () -> lippuService.lunastaLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }


    @Test
    void peruLippuSuccessfully() {
        Lippu lippu = new Lippu();
        lippu.setLipunStatus(Lippu.LippuTila.VARATTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        lippuService.peruLippu(pyynto);

        assertEquals(
                Lippu.LippuTila.PERUTTU,
                lippu.getLipunStatus()
        );

        verify(lippuRepository).save(lippu);
    }

    @Test
    void peruLippuNotFound() {
        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("AAAAAA-111111");

        when(lippuRepository.findByKoodi("AAAAAA-111111"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> lippuService.peruLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }

    @Test
    void peruLippuAlreadyRedeemedThrowsException() {
        Lippu lippu = new Lippu();
        lippu.setLipunStatus(Lippu.LippuTila.LUNASTETTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        assertThrows(
                LippuStatusException.class,
                () -> lippuService.peruLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }

    @Test
    void peruLippuAlreadyCancelledThrowsException() {
        Lippu lippu = new Lippu();
        lippu.setLipunStatus(Lippu.LippuTila.PERUTTU);

        LippuLunastus pyynto = mock(LippuLunastus.class);
        when(pyynto.getKoodi()).thenReturn("ABCDEF-123456");

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        assertThrows(
                LippuStatusException.class,
                () -> lippuService.peruLippu(pyynto)
        );

        verify(lippuRepository, never()).save(any());
    }


    @Test
    void haeLipunTiedotSuccessfully() {
        Lippu lippu = new Lippu();
        lippu.setLippuId(1);
        lippu.setKoodi("ABCDEF-123456");
        lippu.setLipunStatus(Lippu.LippuTila.VARATTU);

        when(lippuRepository.findByKoodi("ABCDEF-123456"))
                .thenReturn(Optional.of(lippu));

        LipunTarkastusVastaus vastaus =
                lippuService.haeLipunTiedot("ABCDEF-123456");

        assertNotNull(vastaus);
    }

    @Test
    void haeLipunTiedotNotFound() {
        when(lippuRepository.findByKoodi("AAAAAA-111111"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> lippuService.haeLipunTiedot("AAAAAA-111111")
        );
    }

    @Test
    void haeLipunTiedotBlankCodeReturnsBadRequest() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> lippuService.haeLipunTiedot(" ")
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


    @Test
    void varaaLiputEventNotFound() {
        LippuVarausPyynto pyynto = mock(LippuVarausPyynto.class);
        when(pyynto.getTapahtumaId()).thenReturn(1L);

        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> lippuService.varaaLiput(pyynto)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(myyntitapahtumaRepository, never()).save(any());
    }

    @Test
    void varaaLiputDeletedEventReturnsNotFound() {
        Tapahtuma tapahtuma = mock(Tapahtuma.class);
        when(tapahtuma.isPoistettu()).thenReturn(true);

        LippuVarausPyynto pyynto = mock(LippuVarausPyynto.class);
        when(pyynto.getTapahtumaId()).thenReturn(1L);

        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.of(tapahtuma));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> lippuService.varaaLiput(pyynto)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void varaaLiputTicketTypeNotFound() {
        Tapahtuma tapahtuma = mock(Tapahtuma.class);
        when(tapahtuma.getTapahtumaId()).thenReturn(1L);

        when(tapahtuma.isPoistettu()).thenReturn(false);
        when(tapahtuma.getMaxLippumaara()).thenReturn(100);

        LippuVaraus varaus = mock(LippuVaraus.class);
        when(varaus.getLipputyyppiId()).thenReturn(1L);

        LippuVarausPyynto pyynto = mock(LippuVarausPyynto.class);
        when(pyynto.getTapahtumaId()).thenReturn(1L);
        when(pyynto.getLiput()).thenReturn(List.of(varaus));

        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.of(tapahtuma));

        when(lippuRepository.countLippuWithoutMaxLippumaara(
                tapahtuma, Lippu.LippuTila.PERUTTU))
                .thenReturn(0L);

        when(lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma))
                .thenReturn(0L);

        when(lipputyyppiRepository.findById(new LipputyyppiId(1, 1)))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> lippuService.varaaLiput(pyynto)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(myyntitapahtumaRepository, never()).save(any());
    }

    @Test
    void varaaLiputExceedingEventCapacityThrowsException() {
        Tapahtuma tapahtuma = mock(Tapahtuma.class);
        when(tapahtuma.getTapahtumaId()).thenReturn(1L);

        when(tapahtuma.isPoistettu()).thenReturn(false);
        when(tapahtuma.getMaxLippumaara()).thenReturn(10);

        Lipputyyppi lipputyyppi = mock(Lipputyyppi.class);
        LipputyyppiId id = new LipputyyppiId(1, 1);

        when(lipputyyppi.getTapahtuma()).thenReturn(tapahtuma);
        when(lipputyyppi.isPoistettu()).thenReturn(false);
        when(lipputyyppi.getMaxLippumaara()).thenReturn(null);
        when(lipputyyppi.getLipunHinta()).thenReturn(new BigDecimal("10.00"));

        LippuVaraus varaus = mock(LippuVaraus.class);
        when(varaus.getLipputyyppiId()).thenReturn(1L);
        when(varaus.getQty()).thenReturn(11);

        LippuVarausPyynto pyynto = mock(LippuVarausPyynto.class);
        when(pyynto.getTapahtumaId()).thenReturn(1L);
        when(pyynto.getLiput()).thenReturn(List.of(varaus));

        when(tapahtumaRepository.findById(1L))
                .thenReturn(Optional.of(tapahtuma));

        when(lippuRepository.countLippuWithoutMaxLippumaara(
                tapahtuma, Lippu.LippuTila.PERUTTU))
                .thenReturn(0L);

        when(lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma))
                .thenReturn(0L);

        when(lipputyyppiRepository.findById(id))
                .thenReturn(Optional.of(lipputyyppi));

        assertThrows(
                LippuCapacityException.class,
                () -> lippuService.varaaLiput(pyynto)
        );

        verify(myyntitapahtumaRepository, never()).save(any());
    }
}
