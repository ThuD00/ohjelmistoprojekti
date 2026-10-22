package ohjelmistoprojekti.lipputoimisto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;

public interface MyyntitapahtumaRepository extends JpaRepository<Myyntitapahtuma, Long> {
    List<Myyntitapahtuma> findAllByMyyja_Id(Long myyjaId);
}
