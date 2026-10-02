package ohjelmistoprojekti.lipputoimisto.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/tapahtumat")
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;
    private final LipputyyppiRepository lipputyyppiRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository, LipputyyppiRepository lipputyyppiRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.lipputyyppiRepository = lipputyyppiRepository;
    }

    @PostMapping
    public ResponseEntity<Tapahtuma> addTapahtuma(
            @Valid @RequestBody Tapahtuma tapahtuma) {

        Tapahtuma tallennettuTapahtuma = tapahtumaRepository.save(tapahtuma);

        return ResponseEntity.status(HttpStatus.CREATED).body(tallennettuTapahtuma);
    }

    @GetMapping
    public Iterable<Tapahtuma> findAllTapahtumat() {
        return tapahtumaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tapahtuma> findById(@PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isPresent()) {
            return ResponseEntity.ok(tapahtuma.get());
        }

        return ResponseEntity.notFound().build();
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTapahtuma(@PathVariable("id") long tapahtumaId) {

        if (!tapahtumaRepository.existsById(tapahtumaId)) {
            return ResponseEntity.notFound().build();
        }

        Tapahtuma loydettyTapahtuma = tapahtumaRepository.findById(tapahtumaId).get();

        if (lipputyyppiRepository.existsByTapahtuma(loydettyTapahtuma)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        tapahtumaRepository.deleteById(tapahtumaId);
        return ResponseEntity.noContent().build();
    }

}
