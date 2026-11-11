package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVastausDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaVastausDto;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.TapahtumaService;
import ohjelmistoprojekti.lipputoimisto.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/tapahtumat")
public class TapahtumaRestController {

    private final TapahtumaService tapahtumaService;

    public TapahtumaRestController(TapahtumaService tapahtumaService) {
        this.tapahtumaService = tapahtumaService;
    }

    @GetMapping
    public ResponseEntity<List<TapahtumaVastausDto>> findAllTapahtumat(
            @RequestParam(defaultValue = "false") boolean poistettu) {

        return ResponseEntity.ok(
                tapahtumaService.getAllTapahtumat(poistettu));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/minun")
    public List<TapahtumaVastausDto> findMyTapahtumat(
            @RequestParam(defaultValue = "false") boolean poistettu) {

        return tapahtumaService.getMyTapahtumat(poistettu);
    }

    @GetMapping("/jarjestaja/{jarjestajaId}")
    public List<TapahtumaVastausDto> findByJarjestaja(
            @PathVariable Long jarjestajaId,
            @RequestParam(defaultValue = "false") boolean poistettu) {

        return tapahtumaService.getByJarjestaja(jarjestajaId, poistettu);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TapahtumaVastausDto> findById(
            @PathVariable("id") long tapahtumaId) {

        return tapahtumaService.getTapahtumaById(tapahtumaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @GetMapping("/{id}/liput")
    public List<LippuVastausDto> findAllLiput(
            @PathVariable("id") long tapahtumaId) {

        return tapahtumaService.getAllLiput(tapahtumaId);
    }

    @PreAuthorize("hasRole('JARJESTAJA')")
    @PostMapping
    public ResponseEntity<TapahtumaVastausDto> addTapahtuma(
            @Valid @RequestBody TapahtumaPyyntoDto pyynto) {

        TapahtumaVastausDto vastaus =
                tapahtumaService.addTapahtuma(pyynto);

        return ResponseEntity.status(HttpStatus.CREATED).body(vastaus);
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TapahtumaVastausDto> updateTapahtuma(
            @PathVariable("id") long tapahtumaId,
            @Valid @RequestBody TapahtumaPyyntoDto pyynto) {

        return ResponseEntity.ok(
                tapahtumaService.updateTapahtuma(tapahtumaId, pyynto));
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTapahtuma(
            @PathVariable("id") long tapahtumaId) {

        tapahtumaService.deleteTapahtuma(tapahtumaId);

        return ResponseEntity.noContent().build();
    }
}
