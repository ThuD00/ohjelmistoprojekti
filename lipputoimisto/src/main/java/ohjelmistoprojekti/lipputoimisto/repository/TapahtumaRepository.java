package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

public interface TapahtumaRepository extends JpaRepository<Tapahtuma, Long> {

}
