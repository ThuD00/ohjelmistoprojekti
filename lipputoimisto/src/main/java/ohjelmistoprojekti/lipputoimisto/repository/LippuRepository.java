package ohjelmistoprojekti.lipputoimisto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Lippu;

public interface LippuRepository extends JpaRepository<Lippu, Long> {
    Optional<Lippu> findByKoodi(String koodi);
}
