package ohjelmistoprojekti.lipputoimisto.security;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import ohjelmistoprojekti.lipputoimisto.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component("tapahtumaSecurity")
public class TapahtumaSecurity {
    private final TapahtumaRepository tapahtumaRepository;
    private final UserContextService userContextService;

    public TapahtumaSecurity(
            TapahtumaRepository tapahtumaRepository,
            UserContextService userContextService) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.userContextService = userContextService;
    }

    public boolean isOwner(long tapahtumaId) {
        Kayttaja kayttaja = userContextService.getCurrentUser();

        Tapahtuma tapahtuma = tapahtumaRepository.findById(tapahtumaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tapahtumaa ei löytynyt"));

        return tapahtuma.getJarjestaja().equals(kayttaja);
    }
}