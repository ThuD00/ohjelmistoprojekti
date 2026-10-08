package ohjelmistoprojekti.lipputoimisto.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;

public class LipputyyppiPyyntoDto {
    @NotEmpty
    private String kuvaus;
    private BigDecimal lipunHinta;

    public LipputyyppiPyyntoDto() {
    }

    public LipputyyppiPyyntoDto(@NotEmpty String kuvaus, BigDecimal lipunHinta) {
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
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
        return "LipputyyppiPyyntoDto [kuvaus=" + kuvaus + ", lipunHinta=" + lipunHinta + "]";
    }

}