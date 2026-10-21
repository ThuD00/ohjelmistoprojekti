package ohjelmistoprojekti.lipputoimisto.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;

public class LipputyyppiVastausDto {
    @Positive 
    private long lipputyyppiId;
    private String kuvaus;
    private BigDecimal lipunHinta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer maxLippumaara;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long liputJaljella;

    public LipputyyppiVastausDto() {
    }

    public LipputyyppiVastausDto(long lipputyyppiId, String kuvaus, BigDecimal lipunHinta, Integer maxLippumaara, Long liputJaljella) {
        this.lipputyyppiId = lipputyyppiId;
        this.kuvaus = kuvaus;
        this.lipunHinta = lipunHinta;
        this.maxLippumaara = maxLippumaara;
        this.liputJaljella = liputJaljella;
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
        return "LipputyyppiVastausDto [lipputyyppiId=" + lipputyyppiId + ", kuvaus=" + kuvaus + ", lipunHinta=" + lipunHinta
                + "]";
    }

    public Integer getMaxLippumaara() {
        return maxLippumaara;
    }

    public void setMaxLippumaara(Integer maxLippumaara) {
        this.maxLippumaara = maxLippumaara;
    }

    public Long getLiputJaljella() {
        return liputJaljella;
    }

    public void setLiputJaljella(Long liputJaljella) {
        this.liputJaljella = liputJaljella;
    }
}
