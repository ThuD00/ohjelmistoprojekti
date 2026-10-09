package ohjelmistoprojekti.lipputoimisto.web;

import java.util.Optional;

import ohjelmistoprojekti.lipputoimisto.service.LippuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu.LippuTila;
import ohjelmistoprojekti.lipputoimisto.dto.LippuLunastus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;

@RestController
@RequestMapping("/api/liput")
public class LippuRestController {

    private final LippuService lippuService;
    private final LippuRepository lippuRepository;

    public LippuRestController(
            LippuRepository lippuRepository,
            LipputyyppiRepository lipputyyppiRepository,
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            TapahtumaRepository tapahtumaRepository, LippuService lippuService) {

        this.lippuRepository = lippuRepository;
        this.lippuService = lippuService;
    }

    @PostMapping("/varaa")
    public ResponseEntity<?> varaaLiput(@Valid @RequestBody LippuVarausPyynto pyynto) {
        return lippuService.varaaLiput(pyynto);
    }

    @PostMapping("/lunasta")
    public ResponseEntity<?> lunastaLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.lunastaLippu(pyynto);
    }

    @PostMapping("/peru")
    public ResponseEntity<?> peruLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.peruLippu(pyynto);
    }
}