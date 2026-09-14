package ohjelmistoprojekti.lipputoimisto.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;

@RestController
@RequestMapping("/api") 
public class LippuRestController {

    private final LippuRepository lippuRepository;

    public LippuRestController(LippuRepository lippuRepository) {
        this.lippuRepository = lippuRepository;
    }
}
