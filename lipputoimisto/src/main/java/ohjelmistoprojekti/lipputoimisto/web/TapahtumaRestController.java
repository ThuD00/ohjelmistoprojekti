package ohjelmistoprojekti.lipputoimisto.web;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;


@RestController 
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
    }

    @GetMapping("/tapahtumat")
    public Iterable<Tapahtuma> findAllTapahtumat() {
        return tapahtumaRepository.findAll();
    }

    @GetMapping("/tapahtumat/{id}")
    public Optional<Tapahtuma> findById(@PathVariable("id") long tapahtumaId) {
        return tapahtumaRepository.findById(tapahtumaId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTapahtuma(@PathVariable("id") long tapahtumaId) {
      tapahtumaRepository.deleteById(tapahtumaId);
    }
    

}
