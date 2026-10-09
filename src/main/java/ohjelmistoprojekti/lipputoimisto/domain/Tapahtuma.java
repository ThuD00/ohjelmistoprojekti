package ohjelmistoprojekti.lipputoimisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "tapahtumaId",
        "aika",
        "paikka",
        "kaupunki",
        "kuvaus",
        "maxLippumaara",
        "lipputyypit"
})

@Entity
public class Tapahtuma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(mappedBy = "tapahtuma")
    @JsonIgnore
    private List<Lipputyyppi> lipputyypit;

    @Column(nullable = false)
    private boolean poistettu = false;

    public Tapahtuma() {
    }


    public Tapahtuma(LocalDateTime aika, String paikka, String kaupunki, String kuvaus,
                     int maxLippumaara, boolean poistettu) {
        this.aika = aika;
        this.paikka = paikka;
        this.kaupunki = kaupunki;
        this.kuvaus = kuvaus;
        this.maxLippumaara = maxLippumaara;
        this.poistettu = poistettu;
    }


    public long getTapahtumaId() {
        return tapahtumaId;
    }


    public void setTapahtumaId(long tapahtumaId) {
        this.tapahtumaId = tapahtumaId;
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

    public List<Lipputyyppi> getLipputyypit() {
        return lipputyypit;
    }


    public void setLipputyypit(List<Lipputyyppi> lipputyypit) {
        this.lipputyypit = lipputyypit;
    }

    @Override
    public String toString() {
        return "Tapahtuma [tapahtumaId=" + tapahtumaId + ", aika=" + aika + ", paikka=" + paikka + ", kaupunki="
                + kaupunki + ", kuvaus=" + kuvaus + ", maxLippumaara=" + maxLippumaara + "]";
    }


    public boolean isPoistettu() {
        return poistettu;
    }

    public void setPoistettu(boolean poistettu) {
        this.poistettu = poistettu;
    }
}
