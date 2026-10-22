package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LippuVastausDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaVastausDto;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.TapahtumaService;
import ohjelmistoprojekti.lipputoimisto.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/tapahtumat")
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;
    private final TapahtumaService tapahtumaService;
    private final UserContextService userContextService;
    private final LippuRepository lippuRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository, TapahtumaService tapahtumaService, UserContextService userContextService, LippuRepository lippuRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.tapahtumaService = tapahtumaService;
        this.userContextService = userContextService;
        this.lippuRepository = lippuRepository;
    }

    @GetMapping
    public ResponseEntity<List<TapahtumaVastausDto>> findAllTapahtumat(
            @RequestParam(defaultValue = "false") boolean poistettu,
            Authentication authentication) {

        boolean isAdmin = authentication != null
                && authentication.getAuthorities().stream()
                    .anyMatch(authority ->
                            authority.getAuthority().equals("ROLE_ADMIN"));

        
        if (poistettu && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<TapahtumaVastausDto> tapahtumat =
            tapahtumaRepository.findAllByPoistettu(poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(t, tapahtumaService.calculateLiputjaljella(t)))
                .toList();

        return ResponseEntity.ok(tapahtumat);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/minun")
    public  List<TapahtumaVastausDto> findMyTapahtumat(
            @RequestParam(defaultValue = "false") boolean poistettu) {

        Kayttaja kayttaja = userContextService.getCurrentUser();

        return tapahtumaRepository
                .findAllByJarjestajaAndPoistettu(kayttaja, poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(t, tapahtumaService.calculateLiputjaljella(t)))
                .toList();
    }

    @GetMapping("/jarjestaja/{jarjestajaId}")
    public List<TapahtumaVastausDto> findByJarjestaja(
            @PathVariable Long jarjestajaId,
            @RequestParam(defaultValue = "false") boolean poistettu) {

        return tapahtumaRepository
                .findAllByJarjestaja_IdAndPoistettu(jarjestajaId, poistettu)
                .stream()
                .map(t -> new TapahtumaVastausDto(t, tapahtumaService.calculateLiputjaljella(t)))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TapahtumaVastausDto> findById(@PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);
        return tapahtuma.map(t -> ResponseEntity.ok(new TapahtumaVastausDto(t, tapahtumaService.calculateLiputjaljella(t))))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @GetMapping("/{id}/liput")
    public List<LippuVastausDto> findAllLiput(@PathVariable("id") long tapahtumaId) {
        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND));
        return lippuRepository.findAllByLipputyyppi_Tapahtuma(tapahtuma).stream().map(LippuVastausDto::new).toList();
    }

    @PreAuthorize("hasRole('JARJESTAJA')")
    @PostMapping
    public ResponseEntity<TapahtumaVastausDto> addTapahtuma(
            @Valid @RequestBody TapahtumaPyyntoDto pyynto) {

        Kayttaja kayttaja = userContextService.getCurrentUser();

        Tapahtuma tapahtuma = tapahtumaRepository.save(new Tapahtuma(
                kayttaja,
                pyynto.getAika(),
                pyynto.getPaikka(),
                pyynto.getKaupunki(),
                pyynto.getKuvaus(),
                pyynto.getMaxLippumaara()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new TapahtumaVastausDto(tapahtuma, tapahtumaService.calculateLiputjaljella(tapahtuma)));
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TapahtumaVastausDto> updateTapahtuma(
            @PathVariable("id") long tapahtumaId,
            @Valid @RequestBody TapahtumaPyyntoDto pyynto) {

        Optional<Tapahtuma> vanhaTapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (vanhaTapahtuma.isPresent()) {
            Tapahtuma tapahtuma = vanhaTapahtuma.get();

            Tapahtuma uusiTapahtuma = new Tapahtuma(
                    pyynto.getAika(),
                    pyynto.getPaikka(),
                    pyynto.getKaupunki(),
                    pyynto.getKuvaus(),
                    pyynto.getMaxLippumaara()
            );

            if (!tapahtumaService.validateTapahtumaMaxLippumaara(tapahtuma, uusiTapahtuma)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            tapahtuma.setAika(uusiTapahtuma.getAika());
            tapahtuma.setPaikka(uusiTapahtuma.getPaikka());
            tapahtuma.setKaupunki(uusiTapahtuma.getKaupunki());
            tapahtuma.setKuvaus(uusiTapahtuma.getKuvaus());
            tapahtuma.setMaxLippumaara(uusiTapahtuma.getMaxLippumaara());

            tapahtuma = tapahtumaRepository.save(tapahtuma);

            return ResponseEntity.ok(new TapahtumaVastausDto(tapahtuma, tapahtumaService.calculateLiputjaljella(tapahtuma)));
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("(hasRole('JARJESTAJA') and @tapahtumaSecurity.isOwner(#tapahtumaId)) or hasRole('ADMIN')")
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
