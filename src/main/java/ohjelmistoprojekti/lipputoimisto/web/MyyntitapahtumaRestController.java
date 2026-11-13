package ohjelmistoprojekti.lipputoimisto.web;

import ohjelmistoprojekti.lipputoimisto.domain.Myyntitapahtuma;

import ohjelmistoprojekti.lipputoimisto.service.MyyntitapahtumaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/myyntitapahtumat")
public class MyyntitapahtumaRestController {

    private final MyyntitapahtumaService myyntitapahtumaService;

    public MyyntitapahtumaRestController(
            MyyntitapahtumaService myyntitapahtumaService) {

        this.myyntitapahtumaService = myyntitapahtumaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Myyntitapahtuma> findAllMyyntitapahtumat() {
        return myyntitapahtumaService.getAllMyyntitapahtumat();
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping("/{id}")
    public ResponseEntity<Myyntitapahtuma> findById(
            @PathVariable("id") long myyntitapahtumaId) {

        return myyntitapahtumaService
                .getMyyntitapahtumaById(myyntitapahtumaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MYYJA')")
    @GetMapping("/minun")
    public List<Myyntitapahtuma> getOmatMyyntitapahtumat() {
        return myyntitapahtumaService.getOmatMyyntitapahtumat();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/myyja/{myyjaId}")
    public List<Myyntitapahtuma> getMyyjanMyyntitapahtumat(
            @PathVariable Long myyjaId) {

        return myyntitapahtumaService
                .getMyyjanMyyntitapahtumat(myyjaId);
    }
}