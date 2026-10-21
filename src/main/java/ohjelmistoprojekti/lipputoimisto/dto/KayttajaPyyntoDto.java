package ohjelmistoprojekti.lipputoimisto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;

public class KayttajaPyyntoDto {

    @NotBlank
    private String nimi;
    @NotBlank
    private String salasana;
    @NotBlank
    private String email;
    @NotNull
    private Kayttaja.KayttajaRooli rooli;

    public KayttajaPyyntoDto() {
    }

    public KayttajaPyyntoDto(String nimi, String salasana, String email, Kayttaja.KayttajaRooli rooli) {
        this.nimi = nimi;
        this.salasana = salasana;
        this.email = email;
        this.rooli = rooli;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
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
