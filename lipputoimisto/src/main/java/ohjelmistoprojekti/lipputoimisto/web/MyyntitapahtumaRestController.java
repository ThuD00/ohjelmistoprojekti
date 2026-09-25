package ohjelmistoprojekti.lipputoimisto.web;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;

@RestController
@RequestMapping ("/api/myyntitapahtumat")
public class MyyntitapahtumaRestController {

  private final MyyntitapahtumaRepository myyntitapahtumaRepository;
  
  public MyyntitapahtumaRestController (MyyntitapahtumaRepository myyntitapahtumaRepository) {
    this.myyntitapahtumaRepository = myyntitapahtumaRepository;
  }

  @GetMapping
  public Iterable<Myyntitapahtuma> findAllMyyntitapahtumat() {
    return myyntitapahtumaRepository.findAll();
  }

  @GetMapping("/{id}")
    public ResponseEntity<Myyntitapahtuma> findById(@PathVariable("id") long myyntitapahtumaId) {
        Optional<Myyntitapahtuma> myyntitapahtuma = myyntitapahtumaRepository.findById(myyntitapahtumaId);

        if (myyntitapahtuma.isPresent()) {
            return ResponseEntity.ok(myyntitapahtuma.get());
        }

        return ResponseEntity.notFound().build();
    }
}
