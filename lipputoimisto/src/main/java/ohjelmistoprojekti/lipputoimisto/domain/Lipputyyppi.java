package ohjelmistoprojekti.lipputoimisto.domain;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity 
public class Lipputyyppi {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lipputyyppiId;

    @ManyToOne 
    @JoinColumn(name = "tapahtumaId", nullable = false)
    private Tapahtuma tapahtuma;

    private String kuvaus;
    
    @Column(name = "lipun_hinta")
    private BigDecimal lipunHinta;

    @OneToMany(mappedBy = "lipputyyppi")
    private List<Lippu> liput;

    public Lipputyyppi() {
    }

    public Lipputyyppi(Tapahtuma tapahtuma, String kuvaus, BigDecimal lipunHinta) {
        this.tapahtuma = tapahtuma;
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
    }

    public Tapahtuma getTapahtuma() {
        return tapahtuma;
    }

    public void setTapahtuma(Tapahtuma tapahtuma) {
        this.tapahtuma = tapahtuma;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public BigDecimal getLipunHinta() {
        return lipunHinta;
    }

    public void setLipunHinta(BigDecimal lipunHinta) {
        this.lipunHinta = lipunHinta;
    }

    public List<Lippu> getLiput() {
        return liput;
    }

    public void setLiput(List<Lippu> liput) {
        this.liput = liput;
    }

    @Override
    public String toString() {
        return "Lipputyyppi [lipputyyppiId=" + lipputyyppiId + ", kuvaus=" + kuvaus
                + ", lipunHinta=" + lipunHinta + "]";
    }

    

    

}
