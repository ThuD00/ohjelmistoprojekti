package ohjelmistoprojekti.lipputoimisto.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({
    "myyntitapahtumaId",
    "myyjaId",
    "maksuaika",
    "summa",
    "liput"
})

@Entity 
public class Myyntitapahtuma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long myyntitapahtumaId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "myyja_id", nullable = false)
    @NotNull
    private Kayttaja myyja;

    private LocalDateTime maksuaika;
    private BigDecimal summa;

    @OneToMany(mappedBy = "myyntitapahtuma")
    private List<Lippu> liput;

    public Myyntitapahtuma() {
    }

    public Myyntitapahtuma(LocalDateTime maksuaika, BigDecimal summa) {
        this.maksuaika = maksuaika;
        this.summa = summa;
    }

    public long getMyyntitapahtumaId() {
        return myyntitapahtumaId;
    }

    public void setMyyntitapahtumaId(long myyntitapahtumaId) {
        this.myyntitapahtumaId = myyntitapahtumaId;
    }

    public LocalDateTime getMaksuaika() {
        return maksuaika;
    }

    public void setMaksuaika(LocalDateTime maksuaika) {
        this.maksuaika = maksuaika;
    }

    public BigDecimal getSumma() {
        return summa;
    }

    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }

    public List<Lippu> getLiput() {
        return liput;
    }

    public void setLiput(List<Lippu> liput) {
        this.liput = liput;
    }

    public Kayttaja getMyyja() {
        return myyja;
    }

    @JsonProperty("myyjaId")
    public Long getMyyjaId() {
        return myyja != null ? myyja.getId() : null;
    }

    public void setMyyja(Kayttaja myyja) {
        this.myyja = myyja;
    }

    @Override
    public String toString() {
        return "Myyntitapahtuma [myyntitapahtumaId=" + myyntitapahtumaId
                + ", myyjaId=" + getMyyjaId()
                + ", maksuaika=" + maksuaika
                + ", summa=" + summa + "]";
    }


}
