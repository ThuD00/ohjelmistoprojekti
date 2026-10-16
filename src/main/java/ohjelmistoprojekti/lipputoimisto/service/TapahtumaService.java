package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.domain.Tapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import ohjelmistoprojekti.lipputoimisto.repository.LipputyyppiRepository;
import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;
import org.springframework.stereotype.Service;

@Service
public class TapahtumaService {
    private final LipputyyppiRepository lipputyyppiRepository;
    private final LippuRepository lippuRepository;

    public TapahtumaService(LipputyyppiRepository lipputyyppiRepository, LippuRepository lippuRepository) {
        this.lipputyyppiRepository = lipputyyppiRepository;
        this.lippuRepository = lippuRepository;
    }

    public boolean validateTapahtumaMaxLippumaara(Tapahtuma tapahtuma, Tapahtuma uusiTapahtuma) {
        long uusiMax = uusiTapahtuma.getMaxLippumaara();

        long sum = 0;

        sum += lippuRepository.countLippuWithoutMaxLippumaara(tapahtuma, Lippu.LippuTila.PERUTTU);
        sum += lipputyyppiRepository.sumMaxLippumaaraByTapahtuma(tapahtuma);

        return sum <= uusiMax;
    }
}
