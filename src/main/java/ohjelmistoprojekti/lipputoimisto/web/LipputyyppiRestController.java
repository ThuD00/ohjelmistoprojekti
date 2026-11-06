package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiVastausDto;
import ohjelmistoprojekti.lipputoimisto.service.LipputyyppiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tapahtumat/{id}/lipputyypit")
public class LipputyyppiRestController {

    private final LipputyyppiService lipputyyppiService;

    public LipputyyppiRestController(LipputyyppiService lipputyyppiService) {
        this.lipputyyppiService = lipputyyppiService;
    }

    @GetMapping
    public ResponseEntity<List<LipputyyppiVastausDto>> getLipputyypit(
            @PathVariable("id") long tapahtumaId,
            Authentication authentication) {

        boolean isAdmin = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        List<LipputyyppiVastausDto> lipputyypit = lipputyyppiService.getLipputyypit(tapahtumaId, isAdmin);

        return ResponseEntity.ok().body(lipputyypit);
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LipputyyppiVastausDto> addLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @Valid @RequestBody LipputyyppiPyyntoDto body) {

        LipputyyppiVastausDto lipputyyppi = lipputyyppiService.addLipputyyppi(tapahtumaId, body);

        return ResponseEntity.status(HttpStatus.CREATED).body(lipputyyppi);
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @PutMapping("/{lipputyyppiId}")
    public ResponseEntity<LipputyyppiVastausDto> updateLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId,
            @Valid @RequestBody LipputyyppiPyyntoDto body) {

        LipputyyppiVastausDto lipputyyppi = lipputyyppiService.updateLipputyyppi(tapahtumaId, lipputyyppiId, body);

        return ResponseEntity.ok().body(lipputyyppi);
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @DeleteMapping("/{lipputyyppiId}")
    public ResponseEntity<Void> deleteLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId) {

        lipputyyppiService.deleteLipputyyppi(tapahtumaId, lipputyyppiId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
