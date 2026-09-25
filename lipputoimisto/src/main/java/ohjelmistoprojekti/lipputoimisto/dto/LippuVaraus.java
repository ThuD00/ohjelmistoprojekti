package ohjelmistoprojekti.lipputoimisto.dto;

public class LippuVaraus {

    private long lipputyyppiId;
    private int qty;

    public LippuVaraus() {
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}