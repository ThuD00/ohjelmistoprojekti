package ohjelmistoprojekti.lipputoimisto.repository;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LippuRepository extends JpaRepository<Lippu, Long> {
    Optional<Lippu> findByKoodi(String koodi);

    boolean existsByLipputyyppi(Lipputyyppi lipputyyppi);

    long countByLipputyyppi(Lipputyyppi lipputyyppi);

    List<Lippu> findAllByLipputyyppi(Lipputyyppi lipputyyppi);

    long countByLipputyyppiAndLipunStatusNot(
            Lipputyyppi lipputyyppi,
            Lippu.LippuTila status
    );

    @Query("select count(l) from Lippu l where l.lipputyyppi.maxLippumaara IS NULL and l.lipputyyppi.tapahtuma = ?1 and l.lipunStatus <> ?2")
    long countLippuWithoutMaxLippumaara(
            Tapahtuma tapahtuma,
            Lippu.LippuTila status
    );
}
