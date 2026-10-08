package ohjelmistoprojekti.lipputoimisto.web;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiDto;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.service.LipputyyppiService;

@RestController
@RequestMapping("/api/tapahtumat/{id}/lipputyypit")
public class LipputyyppiRestController {

    private final LipputyyppiService lipputyyppiService;

    public LipputyyppiRestController(LipputyyppiService lipputyyppiService) {
        this.lipputyyppiService = lipputyyppiService;
    }

    @GetMapping
    public ResponseEntity<List<LipputyyppiDto>> getLipputyypit(
            @PathVariable("id") long tapahtumaId) {

        return lipputyyppiService.getLipputyypit(tapahtumaId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LipputyyppiDto> addLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @Valid @RequestBody LipputyyppiPyyntoDto body) {

        return lipputyyppiService.addLipputyyppi(tapahtumaId, body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{lipputyyppiId}")
    public ResponseEntity<LipputyyppiDto> updateLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId,
            @Valid @RequestBody LipputyyppiDto body) {

        return lipputyyppiService.updateLipputyyppi(tapahtumaId, lipputyyppiId, body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{lipputyyppiId}")
    public ResponseEntity<Void> deleteLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId) {

        return lipputyyppiService.deleteLipputyyppi(tapahtumaId, lipputyyppiId);
    }
}
