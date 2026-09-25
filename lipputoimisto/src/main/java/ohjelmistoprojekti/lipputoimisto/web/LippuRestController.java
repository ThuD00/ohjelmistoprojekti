package ohjelmistoprojekti.lipputoimisto.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVaraus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausVastaus;
import ohjelmistoprojekti.lipputoimisto.dto.VarattuLippu;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;

@RestController
@RequestMapping("/api")
public class LippuRestController {

    private final LippuRepository lippuRepository;
    private final LipputyyppiRepository lipputyyppiRepository;
    private final MyyntitapahtumaRepository myyntitapahtumaRepository;
    private final TapahtumaRepository tapahtumaRepository;

    public LippuRestController(
            LippuRepository lippuRepository,
            LipputyyppiRepository lipputyyppiRepository,
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            TapahtumaRepository tapahtumaRepository) {

        this.lippuRepository = lippuRepository;
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
        this.tapahtumaRepository = tapahtumaRepository;
    }

    @PostMapping("/liput/varaa")
    public ResponseEntity<?> varaaLiput(@RequestBody LippuVarausPyynto pyynto) {

        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(pyynto.getTapahtumaId());

        if (tapahtuma.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BigDecimal summa = BigDecimal.ZERO;

        for (LippuVaraus varaus : pyynto.getLiput()) {

            Optional<Lipputyyppi> lipputyyppi = lipputyyppiRepository.findById(varaus.getLipputyyppiId());

            if (lipputyyppi.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            if (lipputyyppi.get().getTapahtuma().getTapahtumaId() != pyynto.getTapahtumaId()) {
                return ResponseEntity.badRequest().build();
            }

            BigDecimal maara = BigDecimal.valueOf(varaus.getQty());
            summa = summa.add(lipputyyppi.get().getLipunHinta().multiply(maara));
        }

        Myyntitapahtuma myyntitapahtuma = new Myyntitapahtuma(
                LocalDateTime.now(),
                summa);

        myyntitapahtumaRepository.save(myyntitapahtuma);

        List<VarattuLippu> varatutLiput = new ArrayList<>();

        for (LippuVaraus varaus : pyynto.getLiput()) {

            Lipputyyppi lipputyyppi = lipputyyppiRepository
                    .findById(varaus.getLipputyyppiId()).get();

            for (int i = 0; i < varaus.getQty(); i++) {

                Lippu lippu = new Lippu(
                        lipputyyppi,
                        myyntitapahtuma,
                        Lippu.LippuTila.VARATTU);

                lippuRepository.save(lippu);

                varatutLiput.add(
                        new VarattuLippu(
                                lipputyyppi.getLipputyyppiId(),
                                lippu.getKoodi()));
            }
        }

        LippuVarausVastaus vastaus = new LippuVarausVastaus(
                myyntitapahtuma.getMyyntitapahtumaId(),
                summa,
                varatutLiput);

        return ResponseEntity.status(201).body(vastaus);
    }
}