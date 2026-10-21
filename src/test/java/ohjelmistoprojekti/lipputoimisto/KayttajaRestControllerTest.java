package ohjelmistoprojekti.lipputoimisto;

import ohjelmistoprojekti.lipputoimisto.repository.KayttajaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class KayttajaRestControllerTest {
    private static final String BASE_URL = "/api/kayttajat";

    private static final RequestPostProcessor MYYJA =
            httpBasic("myyja", "password");
    private static final RequestPostProcessor JARJESTAJA =
            httpBasic("jarjestaja", "password");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KayttajaRepository kayttajaRepository;

    @Test
    void findAllKayttajatReturnsOkForAdmin() throws Exception {
        mockMvc.perform(get(BASE_URL).with(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.kayttajaId == 1)].rooli").value("ADMIN"))
                .andExpect(jsonPath("$[?(@.kayttajaId == 2)].rooli").value("JARJESTAJA"))
                .andExpect(jsonPath("$[?(@.kayttajaId == 3)].rooli").value("JARJESTAJA"))
                .andExpect(jsonPath("$[?(@.kayttajaId == 4)].rooli").value("MYYJA"));
    }

    @Test
    void findAllKayttajatReturnsForbiddenForMyyja() throws Exception {
        mockMvc.perform(get(BASE_URL).with(MYYJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAllKayttajatReturnsForbiddenForJarjestaja() throws Exception {
        mockMvc.perform(get(BASE_URL).with(JARJESTAJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAllKayttajatReturnsForbiddenWhenUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findKayttajaByIdReturnsOkForJarjestaja() throws Exception {
        mockMvc.perform(get(BASE_URL + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kayttajaId").value(2))
                .andExpect(jsonPath("$.nimi").value("jarjestaja"))
                .andExpect(jsonPath("$.email").value("jarjestaja@example.com"))
                .andExpect(jsonPath("$.rooli").value("JARJESTAJA"));
    }

    @Test
    void findKayttajaByIdReturnsOkForAuthenticatedUser() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1")
                        .with(MYYJA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kayttajaId").value(1))
                .andExpect(jsonPath("$.nimi").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.rooli").value("ADMIN"))
                .andExpect(jsonPath("$.salasana").doesNotExist());
    }

    @Test
    void findKayttajaByIdReturnsNotFoundForAdminWhenUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    void findKayttajaByIdReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createKayttajaReturnsCreated() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "testikayttaja",
                                    "email": "testikayttaja@example.com",
                                    "salasana": "testisalasana",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.kayttajaId").isNumber())
                .andExpect(jsonPath("$.nimi").value("testikayttaja"))
                .andExpect(jsonPath("$.email").value("testikayttaja@example.com"))
                .andExpect(jsonPath("$.rooli").value("MYYJA"))
                .andExpect(jsonPath("$.salasana").doesNotExist());
    }

    @Test
    void createKayttajaReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "",
                                    "email": "not-an-email",
                                    "salasana": "",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createKayttajaReturnsForbiddenForMyyja() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "testikayttaja",
                                    "email": "testikayttaja@example.com",
                                    "salasana": "testisalasana",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void createKayttajaReturnsForbiddenForJarjestaja() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "testikayttaja",
                                    "email": "testikayttaja@example.com",
                                    "salasana": "testisalasana",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void createKayttajaReturnsUnauthorized() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "testikayttaja",
                                    "email": "testikayttaja@example.com",
                                    "salasana": "testisalasana",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateKayttajaReturnsOk() throws Exception {
        mockMvc.perform(put(BASE_URL + "/4")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "paivitetty_myyja",
                                    "email": "paivitetty@example.com",
                                    "salasana": "uusi_testisalasana",
                                    "rooli": "JARJESTAJA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kayttajaId").value(4))
                .andExpect(jsonPath("$.nimi").value("paivitetty_myyja"))
                .andExpect(jsonPath("$.email").value("paivitetty@example.com"))
                .andExpect(jsonPath("$.rooli").value("JARJESTAJA"))
                .andExpect(jsonPath("$.salasana").doesNotExist());
    }

    @Test
    void updateKayttajaReturnsBadRequest() throws Exception {
        mockMvc.perform(put(BASE_URL + "/4")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "",
                                    "email": "not-an-email",
                                    "salasana": "",
                                    "rooli": "MYYJA"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateKayttajaReturnsNotFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/999999")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "paivitetty_kayttaja",
                                    "email": "paivitetty@example.com",
                                    "salasana": "uusi_testisalasana",
                                    "rooli": "JARJESTAJA"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateKayttajaReturnsForbiddenForMyyja() throws Exception {
        mockMvc.perform(put(BASE_URL + "/4")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "paivitetty_kayttaja",
                                    "email": "paivitetty@example.com",
                                    "salasana": "uusi_testisalasana",
                                    "rooli": "JARJESTAJA"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateKayttajaReturnsForbiddenForJarjestaja() throws Exception {
        mockMvc.perform(put(BASE_URL + "/4")
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "paivitetty_kayttaja",
                                    "email": "paivitetty@example.com",
                                    "salasana": "uusi_testisalasana",
                                    "rooli": "JARJESTAJA"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateKayttajaReturnsUnauthorized() throws Exception {
        mockMvc.perform(put(BASE_URL + "/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nimi": "paivitetty_kayttaja",
                                    "email": "paivitetty@example.com",
                                    "salasana": "uusi_testisalasana",
                                    "rooli": "JARJESTAJA"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteKayttajaReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/4").with(ADMIN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteKayttajaReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/999999").with(ADMIN))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteKayttajaReturnsForbiddenForMyyja() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/4").with(MYYJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteKayttajaReturnsForbiddenForJarjestaja() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/4").with(JARJESTAJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteKayttajaReturnsUnauthorized() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/4"))
                .andExpect(status().isUnauthorized());
    }
}