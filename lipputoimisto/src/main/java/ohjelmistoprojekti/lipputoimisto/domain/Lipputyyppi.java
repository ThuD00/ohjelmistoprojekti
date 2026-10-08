package ohjelmistoprojekti.lipputoimisto.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@JsonPropertyOrder({
        "lipputyyppiId",
        "kuvaus",
        "lipunHinta"
})

@Entity
public class Lipputyyppi {

    @EmbeddedId
    private LipputyyppiId id;

    @MapsId("tapahtumaId")
    @ManyToOne
    @JoinColumn(name = "tapahtumaId", nullable = false)
    @JsonIgnore
    private Tapahtuma tapahtuma;

    @NotBlank
    private String kuvaus;

    @NotNull
    @PositiveOrZero
    @Column(name = "lipun_hinta")
    private BigDecimal lipunHinta;

    public Lipputyyppi() {
    }

    public Lipputyyppi(LipputyyppiId id,Tapahtuma tapahtuma, String kuvaus, BigDecimal lipunHinta) {
        this.id = id;
        this.tapahtuma = tapahtuma;
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
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

}
