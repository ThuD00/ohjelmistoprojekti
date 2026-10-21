package ohjelmistoprojekti.lipputoimisto.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "tapahtumaId",
        "jarjestajaId",
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
    private long jarjestajaId;

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

    private long liputJaljella;

    public TapahtumaVastausDto(Tapahtuma tapahtuma, long liputJaljella) {
        this.tapahtumaId = tapahtuma.getTapahtumaId();
        this.jarjestajaId = tapahtuma.getJarjestaja().getId();
        this.aika = tapahtuma.getAika();
        this.paikka = tapahtuma.getPaikka();
        this.kaupunki = tapahtuma.getKaupunki();
        this.kuvaus = tapahtuma.getKuvaus();
        this.maxLippumaara = tapahtuma.getMaxLippumaara();
        this.liputJaljella = liputJaljella;
    }

    public TapahtumaVastausDto(long tapahtumaId, long jarjestajaId, LocalDateTime aika, String paikka, String kaupunki, String kuvaus, int maxLippumaara, long liputJaljella) {
        this.tapahtumaId = tapahtumaId;
        this.jarjestajaId = jarjestajaId;
        this.aika = aika;
        this.paikka = paikka;
        this.kaupunki = kaupunki;
        this.kuvaus = kuvaus;
        this.maxLippumaara = maxLippumaara;
        this.liputJaljella = liputJaljella;
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

    public long getJarjestajaId() {
        return jarjestajaId;
    }

    public void setJarjestajaId(long jarjestajaId) {
        this.jarjestajaId = jarjestajaId;
    }

    public long getLiputJaljella() {
        return liputJaljella;
    }

    public void setLiputJaljella(long liputJaljella) {
        this.liputJaljella = liputJaljella;
    }
}

