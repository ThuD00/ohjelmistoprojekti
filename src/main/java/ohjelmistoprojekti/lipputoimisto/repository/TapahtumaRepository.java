package ohjelmistoprojekti.lipputoimisto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;

import java.util.List;

public interface TapahtumaRepository extends JpaRepository<Tapahtuma, Long> {
    List<Tapahtuma> findAllByPoistettu(boolean poistettu);

}
