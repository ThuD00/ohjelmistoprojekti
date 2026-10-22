package ohjelmistoprojekti.lipputoimisto.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu;

import java.math.BigDecimal;

@JsonPropertyOrder({
        "lippuId",
        "tapahtumaId",
        "lipputyyppiId",
        "myyntitapahtumaId",
        "koodi",
        "hinta",
        "status"})

public class LippuVastausDto {
    private long lippuId;
    private long tapahtumaId;
    private long lipputyyppiId;
    private long myyntitapahtumaId;
    private String koodi;
    private BigDecimal hinta;
    private Lippu.LippuTila status;

    public LippuVastausDto(Lippu lippu) {
        this.lippuId = lippu.getLippuId();
        this.tapahtumaId = lippu.getLipputyyppi().getId().getTapahtumaId();
        this.lipputyyppiId = lippu.getLipputyyppi().getId().getLipputyyppiId();
        this.myyntitapahtumaId = lippu.getMyyntitapahtuma().getMyyntitapahtumaId();
        this.koodi = lippu.getKoodi();
        this.hinta = lippu.getHinta();
        this.status = lippu.getLipunStatus();
    }

    public long getLippuId() {
        return lippuId;
    }

    public void setLippuId(long lippuId) {
        this.lippuId = lippuId;
    }

    public long getTapahtumaId() {
        return tapahtumaId;
    }

    public void setTapahtumaId(long tapahtumaId) {
        this.tapahtumaId = tapahtumaId;
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
    }

    public long getMyyntitapahtumaId() {
        return myyntitapahtumaId;
    }

    public void setMyyntitapahtumaId(long myyntitapahtumaId) {
        this.myyntitapahtumaId = myyntitapahtumaId;
    }

    public String getKoodi() {
        return koodi;
    }

    public void setKoodi(String koodi) {
        this.koodi = koodi;
    }

    public BigDecimal getHinta() {
        return hinta;
    }

    public void setHinta(BigDecimal hinta) {
        this.hinta = hinta;
    }

    public Lippu.LippuTila getStatus() {
        return status;
    }

    public void setStatus(Lippu.LippuTila status) {
        this.status = status;
    }
}
