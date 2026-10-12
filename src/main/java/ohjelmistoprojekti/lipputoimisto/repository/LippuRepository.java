package ohjelmistoprojekti.lipputoimisto.repository;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Lipputyyppi;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
