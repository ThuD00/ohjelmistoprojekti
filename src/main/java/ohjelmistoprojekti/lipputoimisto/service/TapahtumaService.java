package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVastausDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaVastausDto;
import ohjelmistoprojekti.lipputoimisto.exception.LippuCapacityException;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TapahtumaService {

    private final TapahtumaRepository tapahtumaRepository;
    private final LipputyyppiRepository lipputyyppiRepository;
    private final LippuRepository lippuRepository;
    private final UserContextService userContextService;

    public TapahtumaService(
            TapahtumaRepository tapahtumaRepository,
            LipputyyppiRepository lipputyyppiRepository,
            LippuRepository lippuRepository,
            UserContextService userContextService) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.lippuRepository = lippuRepository;
        this.userContextService = userContextService;
    }

    public long calculateLiputjaljella(Tapahtuma tapahtuma) {
        return tapahtuma.getMaxLippumaara() - lippuRepository.countLippuWithoutMaxLippumaara(tapahtuma, Lippu.LippuTila.PERUTTU)
                - lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma);
    }

    public boolean validateTapahtumaMaxLippumaara(Tapahtuma tapahtuma, Tapahtuma uusiTapahtuma) {
        long uusiMax = uusiTapahtuma.getMaxLippumaara();

        long sum = 0;

        sum += lippuRepository.countLippuWithoutMaxLippumaara(tapahtuma, Lippu.LippuTila.PERUTTU);
        sum += lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma);

        return sum <= uusiMax;
    }

    public List<TapahtumaVastausDto> getAllTapahtumat(boolean poistettu) {

        if (poistettu && !userContextService.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return tapahtumaRepository.findAllByPoistettu(poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(
                        t, calculateLiputjaljella(t)))
                .toList();
    }

    public List<TapahtumaVastausDto> getMyTapahtumat(boolean poistettu) {

        Kayttaja kayttaja = userContextService.getCurrentUser();

        return tapahtumaRepository
                .findAllByJarjestajaAndPoistettu(kayttaja, poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(
                        t, calculateLiputjaljella(t)))
                .toList();
    }

    public List<TapahtumaVastausDto> getByJarjestaja(
            Long jarjestajaId, boolean poistettu) {

        return tapahtumaRepository
                .findAllByJarjestaja_IdAndPoistettu(jarjestajaId, poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(
                        t, calculateLiputjaljella(t)))
                .toList();
    }

    public Optional<TapahtumaVastausDto> getTapahtumaById(long tapahtumaId) {

        return tapahtumaRepository.findById(tapahtumaId)
                .map(t -> new TapahtumaVastausDto(
                        t, calculateLiputjaljella(t)));
    }

    public List<LippuVastausDto> getAllLiput(long tapahtumaId) {

        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        return lippuRepository
                .findAllByLipputyyppi_Tapahtuma(tapahtuma)
                .stream()
                .map(LippuVastausDto::new)
                .toList();
    }

    @Transactional
    public TapahtumaVastausDto addTapahtuma(TapahtumaPyyntoDto pyynto) {

        Kayttaja kayttaja = userContextService.getCurrentUser();

        Tapahtuma tapahtuma = new Tapahtuma(
                kayttaja,
                pyynto.getAika(),
                pyynto.getPaikka(),
                pyynto.getKaupunki(),
                pyynto.getKuvaus(),
                pyynto.getMaxLippumaara());

        tapahtuma = tapahtumaRepository.save(tapahtuma);

        return new TapahtumaVastausDto(
                tapahtuma, calculateLiputjaljella(tapahtuma));
    }

    @Transactional
    public TapahtumaVastausDto updateTapahtuma(
            long tapahtumaId, TapahtumaPyyntoDto pyynto) {

        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        Tapahtuma uusiTapahtuma = new Tapahtuma(
                pyynto.getAika(),
                pyynto.getPaikka(),
                pyynto.getKaupunki(),
                pyynto.getKuvaus(),
                pyynto.getMaxLippumaara());

        if (!validateTapahtumaMaxLippumaara(tapahtuma, uusiTapahtuma)) {
            throw new LippuCapacityException("Lippukapasiteetti on pienempi kuin tapahtumaan varatut liput");
        }

        tapahtuma.setAika(uusiTapahtuma.getAika());
        tapahtuma.setPaikka(uusiTapahtuma.getPaikka());
        tapahtuma.setKaupunki(uusiTapahtuma.getKaupunki());
        tapahtuma.setKuvaus(uusiTapahtuma.getKuvaus());
        tapahtuma.setMaxLippumaara(uusiTapahtuma.getMaxLippumaara());

        tapahtuma = tapahtumaRepository.save(tapahtuma);

        return new TapahtumaVastausDto(
                tapahtuma, calculateLiputjaljella(tapahtuma));
    }

    @Transactional
    public void deleteTapahtuma(long tapahtumaId) {

        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        tapahtuma.setPoistettu(true);

        tapahtumaRepository.save(tapahtuma);
    }
}

