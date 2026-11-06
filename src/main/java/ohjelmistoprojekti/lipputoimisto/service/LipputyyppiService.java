package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiVastausDto;
import ohjelmistoprojekti.lipputoimisto.exception.LippuCapacityException;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LipputyyppiService {
    private final LipputyyppiRepository lipputyyppiRepository;
    private final TapahtumaRepository tapahtumaRepository;
    private final LippuRepository lippuRepository;

    public LipputyyppiService(LipputyyppiRepository lipputyyppiRepository, TapahtumaRepository tapahtumaRepository, LippuRepository lippuRepository) {
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.tapahtumaRepository = tapahtumaRepository;
        this.lippuRepository = lippuRepository;
    }

    public long nextLipputyyppiId(long tapahtumaId) { // TODO: tämän voisi toteuttaa paremmin
        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId).get();
        List<Lipputyyppi> lipputyypit = tapahtuma.getLipputyypit();
        Collections.sort(lipputyypit);
        if (lipputyypit.isEmpty()) {
            return 1;
        }
        return tapahtuma.getLipputyypit().getLast().getId().getLipputyyppiId() + 1;
    }

    public Long calculateLiputjaljella(Lipputyyppi lipputyyppi) {
        if (lipputyyppi.getMaxLippumaara() != null) {
            return lipputyyppi.getMaxLippumaara() - lippuRepository.countByLipputyyppiAndLipunStatusNot(lipputyyppi, Lippu.LippuTila.PERUTTU);
        }
        return null;
    }

    public boolean validateMaxLippumaara(Tapahtuma tapahtuma, Lipputyyppi uusiLipputyyppi) {

        Integer uusiMax = uusiLipputyyppi.getMaxLippumaara();

        if (uusiMax == null) {
            return true;
        }

        long jaljella = tapahtuma.getMaxLippumaara();

        for (Lipputyyppi lipputyyppi : tapahtuma.getLipputyypit()) {
            if (lipputyyppi.getId().equals(uusiLipputyyppi.getId())) {
                continue;
            }

            Integer max = lipputyyppi.getMaxLippumaara();

            if (max != null) {
                jaljella -= max;
            } else {
                jaljella -= lippuRepository.countByLipputyyppiAndLipunStatusNot(lipputyyppi, Lippu.LippuTila.PERUTTU);
            }
        }

        long myyty = lippuRepository.countByLipputyyppiAndLipunStatusNot(uusiLipputyyppi, Lippu.LippuTila.PERUTTU);

        if (uusiMax < myyty) {
            return false;
        }

        return jaljella >= uusiMax;
    }


    public List<LipputyyppiVastausDto> getLipputyypit(long tapahtumaId, boolean isAdmin) {

        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findByIdIncludingPoistettu(tapahtumaId);

        if (tapahtuma.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Tapahtuma loydettyTapahtuma = tapahtuma.get();

        if (loydettyTapahtuma.isPoistettu() && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return (
                loydettyTapahtuma.getLipputyypit().stream()
                        .filter(e -> !e.isPoistettu())
                        .map(e -> new LipputyyppiVastausDto(
                                e.getId().getLipputyyppiId(),
                                e.getKuvaus(),
                                e.getLipunHinta(),
                                e.getMaxLippumaara(),
                                calculateLiputjaljella(e)))
                        .toList());
    }

    public LipputyyppiVastausDto addLipputyyppi(long tapahtumaId, LipputyyppiPyyntoDto body) {

        Optional<Tapahtuma> tapahtumaOptional = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtumaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Tapahtuma tapahtuma = tapahtumaOptional.get();

        Lipputyyppi lipputyyppi = new Lipputyyppi(
                new LipputyyppiId(
                        tapahtumaId,
                        nextLipputyyppiId(tapahtumaId)),
                tapahtuma,
                body.getKuvaus(),
                body.getLipunHinta(),
                body.getMaxLippumaara());

        if (!validateMaxLippumaara(tapahtuma, lipputyyppi)) {
            throw new LippuCapacityException("Lipputyypin kapasiteetti on suurempi kuin tapahtuman kokonaiskapasiteetti");
        }

        lipputyyppiRepository.save(lipputyyppi);

        return new LipputyyppiVastausDto(
                lipputyyppi.getId().getLipputyyppiId(),
                lipputyyppi.getKuvaus(),
                lipputyyppi.getLipunHinta(),
                lipputyyppi.getMaxLippumaara(),
                calculateLiputjaljella(lipputyyppi));
    }

    public LipputyyppiVastausDto updateLipputyyppi(long tapahtumaId, long lipputyyppiId, LipputyyppiPyyntoDto body) {

        Optional<Tapahtuma> tapahtumaOptional = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtumaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<Lipputyyppi> lipputyyppiOptional = lipputyyppiRepository.findById(new LipputyyppiId(tapahtumaId, lipputyyppiId));

        if (lipputyyppiOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Lipputyyppi lipputyyppi = lipputyyppiOptional.get();
        Tapahtuma tapahtuma = tapahtumaOptional.get();
        lipputyyppi.setKuvaus(body.getKuvaus());
        lipputyyppi.setLipunHinta(body.getLipunHinta());
        lipputyyppi.setMaxLippumaara(body.getMaxLippumaara());

        if (!validateMaxLippumaara(tapahtuma, lipputyyppi)) {
            throw new LippuCapacityException("Lipputyypin kapasiteetti on suurempi kuin tapahtuman kokonaiskapasiteetti tai pienempi kuin lipputyypin varatut liput");
        }

        lipputyyppiRepository.save(lipputyyppi);

        return (new LipputyyppiVastausDto(
                lipputyyppi.getId().getLipputyyppiId(),
                lipputyyppi.getKuvaus(),
                lipputyyppi.getLipunHinta(),
                lipputyyppi.getMaxLippumaara(),
                calculateLiputjaljella(lipputyyppi)));
    }


    public void deleteLipputyyppi(long tapahtumaId, long lipputyyppiId) {

        Optional<Lipputyyppi> lipputyyppiOptional = lipputyyppiRepository.findById(new LipputyyppiId(tapahtumaId, lipputyyppiId));

        if (lipputyyppiOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }


        Lipputyyppi lipputyyppi = lipputyyppiOptional.get();

        lipputyyppi.setPoistettu(true);
        lipputyyppiRepository.save(lipputyyppi);
    }
}
