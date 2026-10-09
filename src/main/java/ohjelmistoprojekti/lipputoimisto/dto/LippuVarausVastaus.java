package ohjelmistoprojekti.lipputoimisto.dto;

import java.math.BigDecimal;
import java.util.List;

public class LippuVarausVastaus {

    private long myyntitapahtumaId;
    private BigDecimal summa;
    private List<VarattuLippu> liput;

    public LippuVarausVastaus() {
    }

    public LippuVarausVastaus(long myyntitapahtumaId, BigDecimal summa, List<VarattuLippu> liput) {
        this.myyntitapahtumaId = myyntitapahtumaId;
        this.summa = summa;
        this.liput = liput;
    }

    public long getMyyntitapahtumaId() {
        return myyntitapahtumaId;
    }

    public void setMyyntitapahtumaId(long myyntitapahtumaId) {
        this.myyntitapahtumaId = myyntitapahtumaId;
    }

    public BigDecimal getSumma() {
        return summa;
    }

    public void setSumma(BigDecimal summa) {
        this.summa = summa;
    }

    public List<VarattuLippu> getLiput() {
        return liput;
    }

    public void setLiput(List<VarattuLippu> liput) {
        this.liput = liput;
    }
}