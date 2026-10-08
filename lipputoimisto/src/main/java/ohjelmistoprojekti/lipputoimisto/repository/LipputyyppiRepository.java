package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import ohjelmistoprojekti.lipputoimisto.domain.LipputyyppiId;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

public interface LipputyyppiRepository extends JpaRepository<Lipputyyppi, LipputyyppiId> {
    boolean existsByTapahtuma(Tapahtuma tapahtuma);

}

