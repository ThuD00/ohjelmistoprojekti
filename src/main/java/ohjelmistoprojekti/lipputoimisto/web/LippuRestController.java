package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.dto.LippuLunastus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.LippuService;
import ohjelmistoprojekti.lipputoimisto.service.QrService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/liput")
public class LippuRestController {

    private final LippuService lippuService;
    private final QrService qrService;

    public LippuRestController(
            LippuService lippuService,
            QrService qrService) {

        this.lippuService = lippuService;
        this.qrService = qrService;
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/varaa")
    public ResponseEntity<?> varaaLiput(@Valid @RequestBody LippuVarausPyynto pyynto) {
        return lippuService.varaaLiput(pyynto);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/lunasta")
    public ResponseEntity<?> lunastaLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.lunastaLippu(pyynto);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/peru")
    public ResponseEntity<?> peruLippu(@RequestBody LippuLunastus pyynto) {
        return lippuService.peruLippu(pyynto);
    }

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> genQr(@RequestBody LippuLunastus pyynto) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrService.generateQrCode(pyynto.getKoodi()));
    }
}