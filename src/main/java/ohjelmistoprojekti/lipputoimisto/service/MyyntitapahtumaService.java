package ohjelmistoprojekti.lipputoimisto.service;

import ohjelmistoprojekti.lipputoimisto.domain.Kayttaja;
import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyyntitapahtumaService {

    private final MyyntitapahtumaRepository myyntitapahtumaRepository;
    private final UserContextService userContextService;

    public MyyntitapahtumaService(
            MyyntitapahtumaRepository myyntitapahtumaRepository,
            UserContextService userContextService) {

        this.myyntitapahtumaRepository = myyntitapahtumaRepository;
        this.userContextService = userContextService;
    }

    public List<Myyntitapahtuma> getAllMyyntitapahtumat() {
        return myyntitapahtumaRepository.findAll();
    }

    public Optional<Myyntitapahtuma> getMyyntitapahtumaById(
            long myyntitapahtumaId) {

        return myyntitapahtumaRepository.findById(myyntitapahtumaId);
    }

    public List<Myyntitapahtuma> getOmatMyyntitapahtumat() {

        Kayttaja myyja = userContextService.getCurrentUser();

        return myyntitapahtumaRepository.findAllByMyyja_Id(myyja.getId());
    }

    public List<Myyntitapahtuma> getMyyjanMyyntitapahtumat(
            Long myyjaId) {

        return myyntitapahtumaRepository.findAllByMyyja_Id(myyjaId);
    }
}
