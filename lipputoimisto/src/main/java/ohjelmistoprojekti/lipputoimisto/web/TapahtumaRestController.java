package ohjelmistoprojekti.lipputoimisto.web;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/tapahtumat") 
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
    }

    @PostMapping
    public Tapahtuma addTapahtuma(@RequestBody Tapahtuma tapahtuma) {
        return tapahtumaRepository.save(tapahtuma);
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
            @PathVariable ("id") long tapahtumaId,
            @RequestBody Tapahtuma uusiTapahtuma) {

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
    public void deleteTapahtuma(@PathVariable("id") long tapahtumaId) {
    // if (!tapahtumaRepository.existsById(tapahtumaId)) {
    //     tässä pitäisi heittää 404 mutta miten
    // } 
    tapahtumaRepository.deleteById(tapahtumaId);
    }
    

}
