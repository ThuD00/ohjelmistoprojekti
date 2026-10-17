package ohjelmistoprojekti.lipputoimisto.security;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.KayttajaRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component("tapahtumaSecurity")
public class TapahtumaSecurity {
    private final TapahtumaRepository tapahtumaRepository;
    private final KayttajaRepository kayttajaRepository;

    public TapahtumaSecurity(TapahtumaRepository tapahtumaRepository, KayttajaRepository kayttajaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
        this.kayttajaRepository = kayttajaRepository;
    }
    public boolean isOwner(long tapahtumaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException();
        }
        String currentPrincipalName = auth.getName();

        Kayttaja kayttaja = kayttajaRepository.findKayttajaByKayttajanimi(currentPrincipalName);
        if (kayttaja == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Käyttäjää ei löytynyt");
        }

        Optional<Tapahtuma> tapahtumaOptional = tapahtumaRepository.findById(tapahtumaId);
        if (tapahtumaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tapahtumaa ei löytynyt");
        }
        Tapahtuma tapahtuma = tapahtumaOptional.get();

        return tapahtuma.getJarjestaja().equals(kayttaja);
    }
}
