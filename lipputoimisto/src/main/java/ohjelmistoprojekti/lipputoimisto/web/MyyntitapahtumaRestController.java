package ohjelmistoprojekti.lipputoimisto.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;
import ohjelmistoprojekti.lipputoimisto.repository.MyyntitapahtumaRepository;

@RestController
@RequestMapping ("/api")
public class MyyntitapahtumaRestController {

  private final MyyntitapahtumaRepository myyntitapahtumaRepository;
  
  public MyyntitapahtumaRestController (MyyntitapahtumaRepository myyntitapahtumaRepository) {
    this.myyntitapahtumaRepository = myyntitapahtumaRepository;
  }

  @PostMapping("/myyntitapahtumat")
  public Myyntitapahtuma addMyyntitapahtuma(@RequestBody Myyntitapahtuma myyntitapahtuma) {
    return myyntitapahtumaRepository.save(myyntitapahtuma);
  }
}
