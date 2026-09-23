package ohjelmistoprojekti.lipputoimisto.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/tapahtumat")
public class LipputyyppiRestController {

    private final LipputyyppiRepository lipputyyppiRepository;
    private final TapahtumaRepository tapahtumaRepository;

    public LipputyyppiRestController( LipputyyppiRepository lipputyyppiRepository, TapahtumaRepository tapahtumaRepository) {
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.tapahtumaRepository = tapahtumaRepository;
    }

    @GetMapping("/{id}/lipputyypit")
    public ResponseEntity<List<Lipputyyppi>> getLipputyypit( @PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isPresent()) {
            return ResponseEntity.ok(tapahtuma.get().getLipputyypit());
        }

        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/lipputyypit")
    public ResponseEntity<Lipputyyppi> addLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @RequestBody Lipputyyppi lipputyyppi) {

        Optional<Tapahtuma> tapahtuma =
                tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isPresent()) {

            lipputyyppi.setTapahtuma(tapahtuma.get());

            lipputyyppiRepository.save(lipputyyppi);

            return ResponseEntity.ok(lipputyyppi);
        }

        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/lipputyypit/{lipputyyppiId}")
    public ResponseEntity<Lipputyyppi> updateLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId,
            @RequestBody Lipputyyppi uusiLipputyyppi) {

        Optional<Lipputyyppi> vanhaLipputyyppi =
                lipputyyppiRepository.findById(lipputyyppiId);

        if (vanhaLipputyyppi.isPresent()) {

            Lipputyyppi lipputyyppi = vanhaLipputyyppi.get();

            if (lipputyyppi.getTapahtuma().getTapahtumaId() != tapahtumaId) {
                return ResponseEntity.notFound().build();
            }

            lipputyyppi.setKuvaus(uusiLipputyyppi.getKuvaus());
            lipputyyppi.setLipunHinta(uusiLipputyyppi.getLipunHinta());

            lipputyyppiRepository.save(lipputyyppi);

            return ResponseEntity.ok(lipputyyppi);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/lipputyypit/{lipputyyppiId}")
    public ResponseEntity<Void> deleteLipputyyppi(
            @PathVariable("id") long tapahtumaId,
            @PathVariable long lipputyyppiId) {

        Optional<Lipputyyppi> loydettyLipputyyppi =
                lipputyyppiRepository.findById(lipputyyppiId);

        if (loydettyLipputyyppi.isPresent()) {

            Lipputyyppi lipputyyppi = loydettyLipputyyppi.get();

            if (lipputyyppi.getTapahtuma().getTapahtumaId() != tapahtumaId) {
                return ResponseEntity.notFound().build();
            }

            lipputyyppiRepository.deleteById(lipputyyppiId);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}
