package ohjelmistoprojekti.lipputoimisto.dto;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu.LippuTila;

public record LipunTarkastusVastaus(
        long lippuId,
        String koodi,
        LippuTila lipunStatus
) {
}
