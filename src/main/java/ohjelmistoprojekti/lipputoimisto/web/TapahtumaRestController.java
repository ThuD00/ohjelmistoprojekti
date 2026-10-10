package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tapahtumat")
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Tapahtuma> addTapahtuma(
            @Valid @RequestBody Tapahtuma tapahtuma) {

        Tapahtuma tallennettuTapahtuma = tapahtumaRepository.save(tapahtuma);

        return ResponseEntity.status(HttpStatus.CREATED).body(tallennettuTapahtuma);
    }

    @GetMapping
    public Iterable<Tapahtuma> findAllTapahtumat(@RequestParam(defaultValue = "false") boolean poistettu) {
        return tapahtumaRepository.findAllByPoistettu(poistettu);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tapahtuma> findById(@PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        return tapahtuma.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Tapahtuma> updateTapahtuma(
            @PathVariable("id") long tapahtumaId,
            @Valid @RequestBody Tapahtuma uusiTapahtuma) {

        Optional<Tapahtuma> vanhaTapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (vanhaTapahtuma.isPresent()) {
            Tapahtuma tapahtuma = vanhaTapahtuma.get();

            tapahtuma.setAika(uusiTapahtuma.getAika());
            tapahtuma.setPaikka(uusiTapahtuma.getPaikka());
            tapahtuma.setKaupunki(uusiTapahtuma.getKaupunki());
            tapahtuma.setKuvaus(uusiTapahtuma.getKuvaus());
            tapahtuma.setMaxLippumaara(uusiTapahtuma.getMaxLippumaara());

            return ResponseEntity.ok(tapahtumaRepository.save(tapahtuma));
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTapahtuma(@PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Tapahtuma loydettyTapahtuma = tapahtuma.get();

        loydettyTapahtuma.setPoistettu(true);
        tapahtumaRepository.save(loydettyTapahtuma);

        return ResponseEntity.noContent().build();
    }

}
