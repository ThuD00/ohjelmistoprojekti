package ohjelmistoprojekti.lipputoimisto.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TapahtumaRepository extends JpaRepository<Tapahtuma, Long> {
    List<Tapahtuma> findAllByPoistettu(boolean poistettu);

    List<Tapahtuma> findAllByJarjestajaAndPoistettu(
        Kayttaja jarjestaja, boolean poistettu);

    List<Tapahtuma> findAllByJarjestaja_IdAndPoistettu(
        Long jarjestajaId, boolean poistettu);

    @Override
    @NonNull
    @Query("SELECT t FROM Tapahtuma t WHERE t.tapahtumaId = :id AND t.poistettu = false")
    Optional<Tapahtuma> findById(@Param("id") @NonNull Long id);
    @Query("SELECT t FROM Tapahtuma t WHERE t.tapahtumaId = :id")
    Optional<Tapahtuma> findByIdIncludingPoistettu(@Param("id") @NonNull Long id);
}
