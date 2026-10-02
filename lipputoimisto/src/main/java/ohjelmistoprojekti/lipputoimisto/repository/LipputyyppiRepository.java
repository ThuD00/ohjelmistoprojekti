package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

public interface LipputyyppiRepository extends JpaRepository<Lipputyyppi, Long> {
    boolean existsByTapahtuma(Tapahtuma tapahtuma);

}

