package ohjelmistoprojekti.lipputoimisto.web;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.UserContextService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/myyntitapahtumat")
public class MyyntitapahtumaRestController {

    private final MyyntitapahtumaRepository myyntitapahtumaRepository;
    private final UserContextService userContextService;

    public MyyntitapahtumaRestController(
        MyyntitapahtumaRepository myyntitapahtumaRepository,
        UserContextService userContextService) {

        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
        this.userContextService = userContextService;
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

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping("/minun")
    public List<Myyntitapahtuma> getOmatMyyntitapahtumat() {
        Kayttaja myyja = userContextService.getCurrentUser();

        return myyntitapahtumaRepository.findAllByMyyja_Id(myyja.getId());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/myyja/{myyjaId}")
    public List<Myyntitapahtuma> getMyyjanMyyntitapahtumat(
            @PathVariable Long myyjaId) {

        return myyntitapahtumaRepository.findAllByMyyja_Id(myyjaId);
    }

    @GetMapping("/myyja{myyjaId}")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    
}
