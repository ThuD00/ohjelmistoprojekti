package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;

public interface MyyntitapahtumaRepository extends JpaRepository<Myyntitapahtuma, Long> {

}
