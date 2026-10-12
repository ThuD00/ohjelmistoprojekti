package ohjelmistoprojekti.lipputoimisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@JsonPropertyOrder({
        "lipputyyppiId",
        "kuvaus",
        "lipunHinta"
})

@Entity
public class Lipputyyppi implements Comparable<Lipputyyppi> {

    @EmbeddedId
    private LipputyyppiId id;

    @MapsId("tapahtumaId")
    @ManyToOne
    @JoinColumn(name = "tapahtuma_id", nullable = false)
    @JsonIgnore
    private Tapahtuma tapahtuma;

    @NotBlank
    private String kuvaus;

    @NotNull
    @PositiveOrZero
    @Column(name = "lipun_hinta")
    private BigDecimal lipunHinta;

    @Positive
    private Integer maxLippumaara;

    @Column(nullable = false)
    private boolean poistettu = false;

    public Lipputyyppi() {
    }

    public Lipputyyppi(LipputyyppiId id, Tapahtuma tapahtuma, String kuvaus, BigDecimal lipunHinta, Integer maxLippumaara) {
        this.id = id;
        this.tapahtuma = tapahtuma;
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
        this.maxLippumaara = maxLippumaara;
    }

    public LipputyyppiId getId() {
        return id;
    }

    public void setId(LipputyyppiId id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Lipputyyppi [tapahtuma=" + getTapahtuma().getTapahtumaId()
                + ", lipputyyppiId=" + id.getLipputyyppiId()
                + ", kuvaus=" + kuvaus
                + ", lipunHinta=" + lipunHinta + "]";
    }

    public boolean isPoistettu() {
        return poistettu;
    }

    public void setPoistettu(boolean poistettu) {
        this.poistettu = poistettu;
    }

    public Integer getMaxLippumaara() {
        return maxLippumaara;
    }

    public void setMaxLippumaara(Integer maxLippumaara) {
        this.maxLippumaara = maxLippumaara;
    }

    @Override
    public int compareTo(Lipputyyppi lipputyyppi) {
        return Long.compare(this.id.getLipputyyppiId(), lipputyyppi.id.getLipputyyppiId());
    }

}
