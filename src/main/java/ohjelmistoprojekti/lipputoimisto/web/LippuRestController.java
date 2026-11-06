package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.dto.LippuLunastus;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausPyynto;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVarausVastaus;
import ohjelmistoprojekti.lipputoimisto.dto.LipunTarkastusVastaus;
import ohjelmistoprojekti.lipputoimisto.service.LippuService;
import ohjelmistoprojekti.lipputoimisto.service.QrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
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

    @PostMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> genQr(@RequestBody LippuLunastus pyynto) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrService.generateQrCode(pyynto.getKoodi()));
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/varaa")
    public ResponseEntity<?> varaaLiput(@Valid @RequestBody LippuVarausPyynto pyynto) {
        LippuVarausVastaus vastaus = lippuService.varaaLiput(pyynto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vastaus);
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/lunasta")
    public ResponseEntity<?> lunastaLippu(@RequestBody LippuLunastus pyynto) {
        lippuService.lunastaLippu(pyynto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MYYJA')")
    @PostMapping("/peru")
    public ResponseEntity<?> peruLippu(@RequestBody LippuLunastus pyynto) {
        lippuService.peruLippu(pyynto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping
    public ResponseEntity<?> haeLipunTiedot(@RequestParam String code) {

        LipunTarkastusVastaus lippu = lippuService.haeLipunTiedot(code);

        return ResponseEntity.ok().body(lippu);
    }


}