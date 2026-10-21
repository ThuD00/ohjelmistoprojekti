package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.repository.KayttajaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserContextService {
    private final KayttajaRepository kayttajaRepository;

    public UserContextService(KayttajaRepository kayttajaRepository) {
        this.kayttajaRepository = kayttajaRepository;
    }

    public boolean isAuthenticated() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    public Kayttaja getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Tunnistautuminen vaaditaan");
        }

        Kayttaja kayttaja =
                kayttajaRepository.findKayttajaByKayttajanimi(auth.getName());

        if (kayttaja == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Käyttäjää ei löytynyt");
        }

        return kayttaja;
    }
}