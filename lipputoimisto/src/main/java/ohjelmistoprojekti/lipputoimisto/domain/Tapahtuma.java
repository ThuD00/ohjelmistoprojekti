package ohjelmistoprojekti.lipputoimisto.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tapahtuma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tapahtumaId;

    private LocalDateTime aika;
    private String paikka;
    private String kaupunki;
    private String kuvaus;
    private int maxLippumaara;

    @OneToMany(mappedBy = "tapahtuma")
    private List<Lipputyyppi> lipputyypit;
    
    public Tapahtuma() {
    }


    public Tapahtuma(LocalDateTime aika, String paikka, String kaupunki, String kuvaus,
            int maxLippumaara) {
        this.aika = aika;
        this.paikka = paikka;
        this.kaupunki = kaupunki;
        this.kuvaus = kuvaus;
        this.maxLippumaara = maxLippumaara;
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

    

}
