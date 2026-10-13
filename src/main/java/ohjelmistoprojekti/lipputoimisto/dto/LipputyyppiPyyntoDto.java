package ohjelmistoprojekti.lipputoimisto.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class LipputyyppiPyyntoDto {
    @NotEmpty
    private String kuvaus;

    @NotNull
    @PositiveOrZero
    private BigDecimal lipunHinta;

    @Positive
    private Integer maxLippumaara;

    public LipputyyppiPyyntoDto() {
    }

    public LipputyyppiPyyntoDto(@NotEmpty String kuvaus, BigDecimal lipunHinta, Integer maxLippumaara) {
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
        this.maxLippumaara = maxLippumaara;
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

    public Integer getMaxLippumaara() {
        return maxLippumaara;
    }

    public void setMaxLippumaara(Integer maxLippumaara) {
        this.maxLippumaara = maxLippumaara;
    }

    @Override
    public String toString() {
        return "LipputyyppiPyyntoDto [kuvaus=" + kuvaus + ", lipunHinta=" + lipunHinta + ", maxLippumaara" + maxLippumaara + "]";
    }

}