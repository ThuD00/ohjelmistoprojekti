package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.*;
import ohjelmistoprojekti.lipputoimisto.dto.*;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LippuService {

    private final LippuRepository lippuRepository;
    private final LipputyyppiRepository lipputyyppiRepository;
    private final TapahtumaRepository tapahtumaRepository;
    private final MyyntitapahtumaRepository myyntitapahtumaRepository;

    public LippuService(LippuRepository lippuRepository, LipputyyppiRepository lipputyyppiRepository,
                        TapahtumaRepository tapahtumaRepository, MyyntitapahtumaRepository myyntitapahtumaRepository) {
        this.lippuRepository = lippuRepository;
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.tapahtumaRepository = tapahtumaRepository;
        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
    }

    public ResponseEntity<?> varaaLiput(LippuVarausPyynto pyynto) {

        Optional<Tapahtuma> tapahtumaOptional = tapahtumaRepository.findById(pyynto.getTapahtumaId());

        if (tapahtumaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tapahtuma tapahtuma = tapahtumaOptional.get();

        BigDecimal summa = BigDecimal.ZERO;

        for (LippuVaraus varaus : pyynto.getLiput()) {

            Optional<Lipputyyppi> lipputyyppi = lipputyyppiRepository
                    .findById(new LipputyyppiId(tapahtuma.getTapahtumaId(), varaus.getLipputyyppiId()));

            if (lipputyyppi.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (lipputyyppi.get().getTapahtuma().getTapahtumaId() != pyynto.getTapahtumaId()) {
                return ResponseEntity.badRequest().build();
            }

            BigDecimal maara = BigDecimal.valueOf(varaus.getQty());
            summa = summa.add(lipputyyppi.get().getLipunHinta().multiply(maara));
        }
        Myyntitapahtuma myyntitapahtuma = new Myyntitapahtuma(LocalDateTime.now(), summa);
        myyntitapahtumaRepository.save(myyntitapahtuma);

        List<VarattuLippu> varatutLiput = new ArrayList<>();

        for (LippuVaraus varaus : pyynto.getLiput()) {

            Lipputyyppi lipputyyppi = lipputyyppiRepository
                    .findById(new LipputyyppiId(tapahtuma.getTapahtumaId(), varaus.getLipputyyppiId())).get();

            for (int i = 0; i < varaus.getQty(); i++) {

                Lippu lippu = new Lippu(
                        lipputyyppi,
                        myyntitapahtuma,
                        Lippu.LippuTila.VARATTU);

                lippuRepository.save(lippu);

                varatutLiput.add(new VarattuLippu(
                        lipputyyppi.getId().getLipputyyppiId(),
                        lippu.getKoodi()));
            }
        }
        LippuVarausVastaus vastaus = new LippuVarausVastaus(
                myyntitapahtuma.getMyyntitapahtumaId(),
                summa,
                varatutLiput);

        return ResponseEntity.status(HttpStatus.CREATED).body(vastaus);
    }

    private Lippu haeLippu(String koodi) {

        if (koodi == null || koodi.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Lippu> optLippu = lippuRepository.findByKoodi(koodi);

        if (optLippu.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Lippu lippu = optLippu.get();

        Lippu.LippuTila status = lippu.getLipunStatus();

        if (status != Lippu.LippuTila.VARATTU) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return lippu;
    }

    public ResponseEntity<?> lunastaLippu(LippuLunastus pyynto) {
        Lippu lippu = haeLippu(pyynto.getKoodi());
        Lippu.LippuTila status = lippu.getLipunStatus();

        if (status != Lippu.LippuTila.VARATTU) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        lippu.setLipunStatus(Lippu.LippuTila.LUNASTETTU);
        lippuRepository.save(lippu);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> peruLippu(LippuLunastus pyynto) {
        Lippu lippu = haeLippu(pyynto.getKoodi());
        Lippu.LippuTila status = lippu.getLipunStatus();

        if (status != Lippu.LippuTila.VARATTU) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        lippu.setLipunStatus(Lippu.LippuTila.PERUTTU);
        lippuRepository.save(lippu);

        return ResponseEntity.ok().build();
    }
}