package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.repository.KayttajaRepository;
import ohjelmistoprojekti.lipputoimisto.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/kayttajat")
public class KayttajaRestController {
    private final KayttajaRepository kayttajaRepository;
    private final UserContextService userContextService;
    private final BCryptPasswordEncoder passwordEncoder;

    public KayttajaRestController(KayttajaRepository kayttajaRepository,
                                  UserContextService userContextService,
                                  BCryptPasswordEncoder passwordEncoder) {
        this.kayttajaRepository = kayttajaRepository;
        this.userContextService = userContextService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kayttaja> findById(@PathVariable("id") long kayttajaId) {
        userContextService.getCurrentUser(); // tämä heittää 401 jos pyynnön tekijä ei ole tunnistautunut

        Optional<Kayttaja> kayttajaOptional = kayttajaRepository.findById(kayttajaId);
        if (kayttajaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Kayttaja kayttaja = kayttajaOptional.get();
        if (kayttaja.getRooli() != Kayttaja.KayttajaRooli.JARJESTAJA) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(kayttaja);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Kayttaja> findAllKayttajat() {
        return kayttajaRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Kayttaja> addKayttaja(@Valid @RequestBody Kayttaja pyynto) {
        pyynto.setSalasana(passwordEncoder.encode(pyynto.getSalasana()));
        Kayttaja kayttaja = kayttajaRepository.save(pyynto);
        return ResponseEntity.status(HttpStatus.CREATED).body(kayttaja);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Kayttaja> updateKayttaja(@PathVariable("id") long kayttajaId, @Valid @RequestBody Kayttaja pyynto) {
        Optional<Kayttaja> kayttajaOptional = kayttajaRepository.findById(kayttajaId);
        if (kayttajaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Kayttaja kayttaja = kayttajaOptional.get();

        kayttaja.setKayttajanimi(pyynto.getKayttajanimi());
        kayttaja.setSalasana(passwordEncoder.encode(pyynto.getSalasana()));
        kayttaja.setEmail(pyynto.getEmail());
        kayttaja.setRooli(pyynto.getRooli());

        return ResponseEntity.ok().body(kayttajaRepository.save(kayttaja));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteKayttaja(@PathVariable("id") long kayttajaId) {
        if (!kayttajaRepository.existsById(kayttajaId)) {
            return ResponseEntity.notFound().build();
        }
        kayttajaRepository.deleteById(kayttajaId);
        return ResponseEntity.noContent().build();
    }
}
