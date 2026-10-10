package ohjelmistoprojekti.lipputoimisto.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiDto;
import ohjelmistoprojekti.lipputoimisto.dto.LipputyyppiPyyntoDto;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;

@Service
public class LipputyyppiService {
    private final LipputyyppiRepository lipputyyppiRepository;
    private final TapahtumaRepository tapahtumaRepository;
    private final LippuRepository lippuRepository;

    public LipputyyppiService(LipputyyppiRepository lipputyyppiRepository, TapahtumaRepository tapahtumaRepository,
            LippuRepository lippuRepository) {
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.tapahtumaRepository = tapahtumaRepository;
        this.lippuRepository = lippuRepository;
    }

    public long nextLipputyyppiId(long tapahtumaId) { // TODO: tämän voisi toteuttaa paremmin
        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId).get();
        List<Lipputyyppi> lipputyypit = tapahtuma.getLipputyypit();
        Collections.sort(lipputyypit);
        if (lipputyypit.isEmpty()) {
            return 1;
        }
        return tapahtuma.getLipputyypit().getLast().getId().getLipputyyppiId() + 1;
    }

    public ResponseEntity<List<LipputyyppiDto>> getLipputyypit(long tapahtumaId) {
        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isPresent()) {
            return ResponseEntity.ok(tapahtuma.get().getLipputyypit().stream()
                    .map(e -> new LipputyyppiDto(e.getId().getLipputyyppiId(), e.getKuvaus(), e.getLipunHinta()))
                    .toList());
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<LipputyyppiDto> addLipputyyppi(long tapahtumaId, LipputyyppiPyyntoDto body) {

        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isPresent()) {
            Lipputyyppi lipputyyppi = new Lipputyyppi(
                    new LipputyyppiId(tapahtumaId, nextLipputyyppiId(tapahtumaId)),
                    tapahtuma.get(),
                    body.getKuvaus(),
                    body.getLipunHinta());
            lipputyyppiRepository.save(lipputyyppi);

            return ResponseEntity.status(HttpStatus.CREATED).body(new LipputyyppiDto(
                    lipputyyppi.getId().getLipputyyppiId(), lipputyyppi.getKuvaus(), lipputyyppi.getLipunHinta()));
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<LipputyyppiDto> updateLipputyyppi(long tapahtumaId, long lipputyyppiId, LipputyyppiPyyntoDto body) {

        Optional<Tapahtuma> tapahtuma = tapahtumaRepository.findById(tapahtumaId);

        if (tapahtuma.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Lipputyyppi> lipputyyppiOptional = lipputyyppiRepository
                .findById(new LipputyyppiId(tapahtumaId, lipputyyppiId));

        if (lipputyyppiOptional.isPresent()) {
            Lipputyyppi lipputyyppi = lipputyyppiOptional.get();
            lipputyyppi.setKuvaus(body.getKuvaus());
            lipputyyppi.setLipunHinta(body.getLipunHinta());
            lipputyyppiRepository.save(lipputyyppi);

            return ResponseEntity.ok().body(new LipputyyppiDto(
                    lipputyyppi.getId().getLipputyyppiId(), lipputyyppi.getKuvaus(), lipputyyppi.getLipunHinta()));
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Void> deleteLipputyyppi(long tapahtumaId, long lipputyyppiId) {

        Optional<Lipputyyppi> lipputyyppiOptional = lipputyyppiRepository
                .findById(new LipputyyppiId(tapahtumaId, lipputyyppiId));

        if (lipputyyppiOptional.isPresent()) {

            Lipputyyppi lipputyyppi = lipputyyppiOptional.get();

            if (lippuRepository.existsByLipputyyppi(lipputyyppi)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            lipputyyppiRepository
                    .deleteById(lipputyyppi.getId());

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
