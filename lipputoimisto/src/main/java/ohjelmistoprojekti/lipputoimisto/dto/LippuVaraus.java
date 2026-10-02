package ohjelmistoprojekti.lipputoimisto.dto;

import jakarta.validation.constraints.Positive;

public class LippuVaraus {

    @Positive 
    private long lipputyyppiId;

    @Positive
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