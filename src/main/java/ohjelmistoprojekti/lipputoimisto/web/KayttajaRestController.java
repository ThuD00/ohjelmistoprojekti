package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.dto.KayttajaPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.KayttajaVastausDto;
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
    public ResponseEntity<KayttajaVastausDto> findById(@PathVariable("id") long kayttajaId) {
        Optional<Kayttaja> kayttajaOptional = kayttajaRepository.findById(kayttajaId);
        if (kayttajaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Kayttaja kayttaja = kayttajaOptional.get();
        if (kayttaja.getRooli() != Kayttaja.KayttajaRooli.JARJESTAJA
                && !userContextService.isAuthenticated()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(new KayttajaVastausDto(kayttaja));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<KayttajaVastausDto> findAllKayttajat() {
        return kayttajaRepository.findAll().stream().map(KayttajaVastausDto::new).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KayttajaVastausDto> addKayttaja(@Valid @RequestBody KayttajaPyyntoDto pyynto) {
        Kayttaja kayttaja = new Kayttaja(
                pyynto.getNimi(),
                passwordEncoder.encode(pyynto.getSalasana()),
                pyynto.getEmail(),
                pyynto.getRooli()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(new KayttajaVastausDto(kayttajaRepository.save(kayttaja)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<KayttajaVastausDto> updateKayttaja(@PathVariable("id") long kayttajaId, @Valid @RequestBody KayttajaPyyntoDto pyynto) {
        Optional<Kayttaja> kayttajaOptional = kayttajaRepository.findById(kayttajaId);
        if (kayttajaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Kayttaja kayttaja = kayttajaOptional.get();

        kayttaja.setKayttajanimi(pyynto.getNimi());
        kayttaja.setSalasana(passwordEncoder.encode(pyynto.getSalasana()));
        kayttaja.setEmail(pyynto.getEmail());
        kayttaja.setRooli(pyynto.getRooli());

        return ResponseEntity.ok().body((new KayttajaVastausDto(kayttajaRepository.save(kayttaja))));
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
