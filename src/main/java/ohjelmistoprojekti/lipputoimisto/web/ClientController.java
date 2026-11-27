package ohjelmistoprojekti.lipputoimisto.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping({ "/", "/index" })
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/lipunmyynti")
    public String lipunmyynti() {
        return "forward:/lipunmyynti.html";
    }

    @GetMapping("/tapahtumat")
    public String tapahtumat() {
        return "forward:/tapahtumat.html";
    }

    @GetMapping("/raportit")
    public String raportit() {
        return "forward:/raportit.html";
    }

}
