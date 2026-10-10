package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;

public interface KayttajaRepository extends JpaRepository <Kayttaja, Long> {

}
