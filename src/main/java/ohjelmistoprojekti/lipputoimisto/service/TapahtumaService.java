package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import org.springframework.stereotype.Service;

@Service
public class TapahtumaService {
    public boolean isOwner(Tapahtuma tapahtuma, Kayttaja kayttaja) {
        if (kayttaja.getRooli() == Kayttaja.KayttajaRooli.ADMIN) {
            return true;
        }
        return kayttaja == tapahtuma.getJarjestaja();
    }
}
