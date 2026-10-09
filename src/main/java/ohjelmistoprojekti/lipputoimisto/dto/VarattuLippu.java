package ohjelmistoprojekti.lipputoimisto.dto;

public class VarattuLippu {

    private long lipputyyppiId;
    private String koodi;

    public VarattuLippu() {
    }

    public VarattuLippu(long lipputyyppiId, String koodi) {
        this.lipputyyppiId = lipputyyppiId;
        this.koodi = koodi;
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
    }

    public String getKoodi() {
        return koodi;
    }

    public void setKoodi(String koodi) {
        this.koodi = koodi;
    }
}