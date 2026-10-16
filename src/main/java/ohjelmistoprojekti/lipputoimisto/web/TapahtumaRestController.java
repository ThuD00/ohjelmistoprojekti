package ohjelmistoprojekti.lipputoimisto.web;

import jakarta.validation.Valid;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.dto.TapahtumaVastausDto;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.TapahtumaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tapahtumat")
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;
    private final TapahtumaService tapahtumaService;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository, TapahtumaService tapahtumaService) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.tapahtumaService = tapahtumaService;
    }

    @GetMapping
    public Iterable<TapahtumaVastausDto> findAllTapahtumat(@RequestParam(defaultValue = "false") boolean poistettu) {
        return tapahtumaRepository.findAllByPoistettu(poistettu).stream().map(TapahtumaVastausDto::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TapahtumaVastausDto> findById(@PathVariable("id") long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);
        return tapahtuma.map(value -> ResponseEntity.ok(new TapahtumaVastausDto(value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PreAuthorize("hasRole('JARJESTAJA')")
    @PostMapping
    public ResponseEntity<TapahtumaVastausDto> addTapahtuma(
            @Valid @RequestBody TapahtumaPyyntoDto pyynto) {

        Tapahtuma tapahtuma = tapahtumaRepository.save(new Tapahtuma(
                pyynto.getAika(),
                pyynto.getPaikka(),
                pyynto.getKaupunki(),
                pyynto.getKuvaus(),
                pyynto.getMaxLippumaara()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new TapahtumaVastausDto(tapahtuma));
    }

    @PreAuthorize("hasRole('JARJESTAJA')")
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

            return ResponseEntity.ok(new TapahtumaVastausDto(tapahtumaRepository.save(tapahtuma)));
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('JARJESTAJA')")
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
