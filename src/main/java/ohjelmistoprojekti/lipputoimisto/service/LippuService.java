package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.*;
import ohjelmistoprojekti.lipputoimisto.dto.*;
import ohjelmistoprojekti.lipputoimisto.exception.LippuCapacityException;
import ohjelmistoprojekti.lipputoimisto.exception.LippuStatusException;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LippuService {

    private final LippuRepository lippuRepository;
    private final LipputyyppiRepository lipputyyppiRepository;
    private final TapahtumaRepository tapahtumaRepository;
    private final MyyntitapahtumaRepository myyntitapahtumaRepository;
    private final UserContextService userContextService;

    public LippuService(LippuRepository lippuRepository, LipputyyppiRepository lipputyyppiRepository,
                        TapahtumaRepository tapahtumaRepository, MyyntitapahtumaRepository myyntitapahtumaRepository,
                        UserContextService userContextService) {
        this.lippuRepository = lippuRepository;
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.tapahtumaRepository = tapahtumaRepository;
        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
        this.userContextService = userContextService;
    }

    public LippuVarausVastaus varaaLiput(LippuVarausPyynto pyynto) {

        Optional<Tapahtuma> tapahtumaOptional = tapahtumaRepository.findById(pyynto.getTapahtumaId());

        if (tapahtumaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Tapahtuma tapahtuma = tapahtumaOptional.get();

        if (tapahtuma.isPoistettu()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        BigDecimal summa = BigDecimal.ZERO;

        List<Lippu> liput = new ArrayList<>();

        long tapahtumaMax = tapahtuma.getMaxLippumaara() - lippuRepository.countLippuWithoutMaxLippumaara(tapahtuma, Lippu.LippuTila.PERUTTU)
                - lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma);
        Map<Lipputyyppi, Integer> lippuQty = new HashMap<>();

        for (LippuVaraus varaus : pyynto.getLiput()) {

            Optional<Lipputyyppi> lipputyyppiOptional = lipputyyppiRepository
                    .findById(new LipputyyppiId(tapahtuma.getTapahtumaId(), varaus.getLipputyyppiId()));

            if (lipputyyppiOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Lipputyyppi lipputyyppi = lipputyyppiOptional.get();

            if (lipputyyppi.isPoistettu()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (lipputyyppi.getTapahtuma().getTapahtumaId() != pyynto.getTapahtumaId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }


            for (int i = 0; i < varaus.getQty(); i++) {
                liput.add(new Lippu(lipputyyppi, lipputyyppi.getLipunHinta()));
                lippuQty.put(lipputyyppi, lippuQty.getOrDefault(lipputyyppi, 0) + 1);
                summa = summa.add(lipputyyppi.getLipunHinta());
            }
        }

        for (Map.Entry<Lipputyyppi, Integer> e : lippuQty.entrySet()) {
            Lipputyyppi lipputyyppi = e.getKey();
            Integer qty = e.getValue();
            if (lipputyyppi.getMaxLippumaara() == null) {
                if (tapahtumaMax < qty) {
                    throw new LippuCapacityException("Tapahtumaan ei ole tarpeeksi lippuja jäljellä");
                }
                tapahtumaMax -= qty;
            } else {
                long liputJaljella = lipputyyppi.getMaxLippumaara() - lippuRepository.countByLipputyyppiAndLipunStatusNot(lipputyyppi, Lippu.LippuTila.PERUTTU);
                if (liputJaljella < qty) {
                    throw new LippuCapacityException("Lipputyyppiin ei ole tarpeeksi lippuja jäljellä");
                }
            }
        }

        Myyntitapahtuma myyntitapahtuma = new Myyntitapahtuma(LocalDateTime.now(), summa);

        myyntitapahtuma.setMyyja(userContextService.getCurrentUser());

        myyntitapahtumaRepository.save(myyntitapahtuma);

        List<VarattuLippu> varatutLiput = new ArrayList<>();

        for (Lippu lippu : liput) {

            lippu.setMyyntitapahtuma(myyntitapahtuma);

            lippuRepository.save(lippu);

            varatutLiput.add(new VarattuLippu(
                    lippu.getLipputyyppi().getId().getLipputyyppiId(),
                    lippu.getKoodi()));
        }

        return new LippuVarausVastaus(
                myyntitapahtuma.getMyyntitapahtumaId(),
                summa,
                varatutLiput);
    }

    private Lippu haeLippu(String koodi) {

        if (koodi == null || koodi.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Lippu> optLippu = lippuRepository.findByKoodi(koodi);

        if (optLippu.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optLippu.get();
    }

    public void lunastaLippu(LippuLunastus pyynto) {
        Lippu lippu = haeLippu(pyynto.getKoodi());
        Lippu.LippuTila status = lippu.getLipunStatus();

        if (status != Lippu.LippuTila.VARATTU) {
            throw new LippuStatusException("Lippu on jo lunastettu tai peruttu");
        }

        lippu.setLipunStatus(Lippu.LippuTila.LUNASTETTU);
        lippuRepository.save(lippu);
    }

    public void peruLippu(LippuLunastus pyynto) {
        Lippu lippu = haeLippu(pyynto.getKoodi());
        Lippu.LippuTila status = lippu.getLipunStatus();

        if (status != Lippu.LippuTila.VARATTU) {
            throw new LippuStatusException("Lippu on jo lunastettu tai peruttu");
        }

        lippu.setLipunStatus(Lippu.LippuTila.PERUTTU);
        lippuRepository.save(lippu);
    }

    public LipunTarkastusVastaus haeLipunTiedot(String koodi) {

        if (koodi == null || koodi.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

        Lippu lippu = lippuRepository.findByKoodi(koodi)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Lippua ei löydy"));

        return new LipunTarkastusVastaus(
            lippu.getLippuId(),
            lippu.getKoodi(),
            lippu.getLipunStatus()
    );
    }

}