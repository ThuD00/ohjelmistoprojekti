package ohjelmistoprojekti.lipputoimisto.dto;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;

public class KayttajaVastausDto {
    private long kayttajaId;
    private String nimi;
    private String email;
    private Kayttaja.KayttajaRooli rooli;

    public KayttajaVastausDto() {
    }

    public KayttajaVastausDto(long kayttajaId, String nimi, String email, Kayttaja.KayttajaRooli rooli) {
        this.kayttajaId = kayttajaId;
        this.nimi = nimi;
        this.email = email;
        this.rooli = rooli;
    }

    public KayttajaVastausDto(Kayttaja kayttaja) {
        this.kayttajaId = kayttaja.getId();
        this.nimi = kayttaja.getKayttajanimi();
        this.email = kayttaja.getEmail();
        this.rooli = kayttaja.getRooli();
    }

    public long getKayttajaId() {
        return kayttajaId;
    }

    public void setKayttajaId(long kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Kayttaja.KayttajaRooli getRooli() {
        return rooli;
    }

    public void setRooli(Kayttaja.KayttajaRooli rooli) {
        this.rooli = rooli;
    }
}
