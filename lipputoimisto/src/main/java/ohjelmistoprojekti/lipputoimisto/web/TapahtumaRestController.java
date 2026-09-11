package ohjelmistoprojekti.lipputoimisto.web;

import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.repository.TapahtumaRepository;

@RestController 
public class TapahtumaRestController {

    private final TapahtumaRepository tapahtumaRepository;

    public TapahtumaRestController(TapahtumaRepository tapahtumaRepository) {
        this.tapahtumaRepository = tapahtumaRepository;
    }

}
