package ohjelmistoprojekti.lipputoimisto.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "tapahtumaId",
        "aika",
        "paikka",
        "kaupunki",
        "kuvaus",
        "maxLippumaara"
})

public class TapahtumaVastausDto {
    @NotNull
    private long tapahtumaId;

    @NotNull
    private LocalDateTime aika;

    @NotBlank
    private String paikka;

    @NotBlank
    private String kaupunki;

    @NotBlank
    private String kuvaus;

    @Positive
    private int maxLippumaara;

    public TapahtumaVastausDto(Tapahtuma tapahtuma) {
        this.tapahtumaId = tapahtuma.getTapahtumaId();
        this.aika = tapahtuma.getAika();
        this.paikka = tapahtuma.getPaikka();
        this.kaupunki = tapahtuma.getKaupunki();
        this.kuvaus = tapahtuma.getKuvaus();
        this.maxLippumaara = tapahtuma.getMaxLippumaara();
    }

    public TapahtumaVastausDto(long tapahtumaId, LocalDateTime aika, String paikka, String kaupunki, String kuvaus, int maxLippumaara) {
        this.tapahtumaId = tapahtumaId;
        this.aika = aika;
        this.paikka = paikka;
        this.kaupunki = kaupunki;
        this.kuvaus = kuvaus;
        this.maxLippumaara = maxLippumaara;
    }

    public LocalDateTime getAika() {
        return aika;
    }

    public void setAika(LocalDateTime aika) {
        this.aika = aika;
    }

    public String getPaikka() {
        return paikka;
    }

    public void setPaikka(String paikka) {
        this.paikka = paikka;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int getMaxLippumaara() {
        return maxLippumaara;
    }

    public void setMaxLippumaara(int maxLippumaara) {
        this.maxLippumaara = maxLippumaara;
    }

    @Override
    public String toString() {
        return "TapahtumaPyyntoDto{" +
                "aika=" + aika +
                ", paikka='" + paikka + '\'' +
                ", kaupunki='" + kaupunki + '\'' +
                ", kuvaus='" + kuvaus + '\'' +
                ", maxLippumaara=" + maxLippumaara +
                '}';
    }

    public long getTapahtumaId() {
        return tapahtumaId;
    }

    public void setTapahtumaId(long tapahtumaId) {
        this.tapahtumaId = tapahtumaId;
    }
}

