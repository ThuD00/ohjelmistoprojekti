package ohjelmistoprojekti.lipputoimisto.domain;

import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Lippu {

    private static final String KOODIMERKIT = "ABCDEFGHJKMNPQRSTVWXYZ";

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

    @Column(name = "koodi", unique = true, nullable = false)
    private String koodi;

    @Column(name = "lipun_status")
    @Enumerated(EnumType.STRING)
    private LippuTila lipunStatus;

    public Lippu() {
    }

    public Lippu(Lipputyyppi lipputyyppi, Myyntitapahtuma myyntitapahtuma, LippuTila lipunStatus) {
        this.lipputyyppi = lipputyyppi;
        this.myyntitapahtuma = myyntitapahtuma;
        this.lipunStatus = lipunStatus;
    }

    @PrePersist
    private void luoKoodi() {
        Random random = new Random();
        StringBuilder koodiBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            koodiBuilder.append(KOODIMERKIT.charAt(random.nextInt(KOODIMERKIT.length())));
        }
        koodiBuilder.append("-");
        for (int i = 0; i < 6; i++) {
            koodiBuilder.append(random.nextInt(10));
        }

        koodi = koodiBuilder.toString();
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
        return "Lippu [lippuId=" + lippuId + ", lipputyyppi=" + lipputyyppi + ", myyntitapahtuma=" + myyntitapahtuma
                + ", koodi=" + koodi + ", lipunStatus=" + lipunStatus + "]";
    }

}
