package ohjelmistoprojekti.lipputoimisto.web;

import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/myyntitapahtumat")
public class MyyntitapahtumaRestController {

    private final MyyntitapahtumaRepository myyntitapahtumaRepository;

    public MyyntitapahtumaRestController(MyyntitapahtumaRepository myyntitapahtumaRepository) {
        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping
    public Iterable<Myyntitapahtuma> findAllMyyntitapahtumat() {
        return myyntitapahtumaRepository.findAll();
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping("/{id}")
    public ResponseEntity<Myyntitapahtuma> findById(@PathVariable("id") long myyntitapahtumaId) {
        Optional<Myyntitapahtuma> myyntitapahtuma = myyntitapahtumaRepository.findById(myyntitapahtumaId);

        if (myyntitapahtuma.isPresent()) {
            return ResponseEntity.ok(myyntitapahtuma.get());
        }

        return ResponseEntity.notFound().build();
    }
}
