package ohjelmistoprojekti.lipputoimisto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class Lippu {

    public enum LippuTila {
        VARATTU,
        LUNASTETTU,
        PERUTTU
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lippuId;

    @ManyToOne 
    @JoinColumn(name = "lipputyyppiId", nullable = false)
    private Lipputyyppi lipputyyppi;

    @ManyToOne 
    @JoinColumn(name = "myyntitapahtumaId", nullable = false)
    private Myyntitapahtuma myyntitapahtuma;

    private String koodi;

    @Column(name = "lipun_status")
    @Enumerated(EnumType.STRING)
    private LippuTila lipunStatus;

    public Lippu() {
    }

    public Lippu(Lipputyyppi lipputyyppi, Myyntitapahtuma myyntitapahtuma, String koodi, LippuTila lipunStatus) {
        this.lipputyyppi = lipputyyppi;
        this.myyntitapahtuma = myyntitapahtuma;
        this.koodi = koodi;
        this.lipunStatus = lipunStatus;
    }

    public long getLippuId() {
        return lippuId;
    }

    public void setLippuId(long lippuId) {
        this.lippuId = lippuId;
    }

    public Lipputyyppi getLipputyyppi() {
        return lipputyyppi;
    }

    public void setLipputyyppi(Lipputyyppi lipputyyppi) {
        this.lipputyyppi = lipputyyppi;
    }

    public Myyntitapahtuma getMyyntitapahtuma() {
        return myyntitapahtuma;
    }

    public void setMyyntitapahtuma(Myyntitapahtuma myyntitapahtuma) {
        this.myyntitapahtuma = myyntitapahtuma;
    }

    public String getKoodi() {
        return koodi;
    }

    public void setKoodi(String koodi) {
        this.koodi = koodi;
    }

    public LippuTila getLipunStatus() {
        return lipunStatus;
    }

    public void setLipunStatus(LippuTila lipunStatus) {
        this.lipunStatus = lipunStatus;
    }

    @Override
    public String toString() {
        return "Lippu [lippuId=" + lippuId + ", koodi=" + koodi + ", lipunStatus=" + lipunStatus + "]";
    }

    
}
