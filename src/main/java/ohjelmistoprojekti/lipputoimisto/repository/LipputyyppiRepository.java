package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import org.springframework.data.jpa.repository.Query;

public interface LipputyyppiRepository extends JpaRepository<Lipputyyppi, LipputyyppiId> {
    boolean existsByTapahtuma(Tapahtuma tapahtuma);

    @Query("select COALESCE(SUM(l.maxLippumaara), 0) from Lipputyyppi l where l.tapahtuma = ?1 AND l.maxLippumaara IS NOT NULL")
    long sumMaxLippumaaraByTapahtuma(Tapahtuma tapahtuma);
}

