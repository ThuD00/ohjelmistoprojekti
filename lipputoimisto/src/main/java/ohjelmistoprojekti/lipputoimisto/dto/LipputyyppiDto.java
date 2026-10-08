package ohjelmistoprojekti.lipputoimisto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;

public class LipputyyppiDto {
    @NotEmpty
    private long lipputyyppiId;
    private String kuvaus;
    private BigDecimal lipunHinta;

    public LipputyyppiDto() {
    }

    public LipputyyppiDto(@NotEmpty long lipputyyppiId, String kuvaus, BigDecimal lipunHinta) {
        this.lipputyyppiId = lipputyyppiId;
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
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
        return "LipputyyppiDto [lipputyyppiId=" + lipputyyppiId + ", kuvaus=" + kuvaus + ", lipunHinta=" + lipunHinta
                + "]";
    }

}
