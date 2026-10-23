
package ohjelmistoprojekti.lipputoimisto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
class TapahtumaRestControllerTest {

    private static final String BASE_URL = "/api/tapahtumat";

    private static final RequestPostProcessor MYYJA =
            httpBasic("myyja", "password");
    private static final RequestPostProcessor JARJESTAJA =
            httpBasic("jarjestaja", "password");
    private static final RequestPostProcessor JARJESTAJA1 =
            httpBasic("jarjestaja1", "password");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "password");

    @Autowired
    private MockMvc mockMvc;

    // TODO: testaa myös tapahtuman omistajuus

    @Test
    void getTapahtumatReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].tapahtumaId").value(1))
                .andExpect(jsonPath("$[0].jarjestajaId").value(2))
                .andExpect(jsonPath("$[0].aika").value("2026-10-05T20:00:00"))
                .andExpect(jsonPath("$[0].paikka").value("Tavastia"))
                .andExpect(jsonPath("$[0].kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$[0].kuvaus").value("Stand-up Comedy Night"))
                .andExpect(jsonPath("$[0].maxLippumaara").value(700))
                .andExpect(jsonPath("$[0].liputJaljella").value(697));
    }

    @Test
    void getDeletedTapahtumatReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "?poistettu=true")
                        .with(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tapahtumaId").value(4))
                .andExpect(jsonPath("$[0].jarjestajaId").value(3))
                .andExpect(jsonPath("$[0].aika").value("2026-11-20T19:00:00"))
                .andExpect(jsonPath("$[0].paikka").value("Helsingin jäähalli"))
                .andExpect(jsonPath("$[0].kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$[0].kuvaus").value("Winter Rock Night"))
                .andExpect(jsonPath("$[0].maxLippumaara").value(8500))
                .andExpect(jsonPath("$[0].liputJaljella").value(8500));
    }

    @Test
    void getDeletedTapahtumatReturnsForbidden() throws Exception {
        mockMvc.perform(get(BASE_URL + "?poistettu=true")
                        .with(MYYJA))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMinunTapahtumatReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/minun")
                        .with(JARJESTAJA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[0].tapahtumaId").value(1))
                .andExpect(jsonPath("$[0].jarjestajaId").value(2))
                .andExpect(jsonPath("$[0].aika").value("2026-10-05T20:00:00"))
                .andExpect(jsonPath("$[0].paikka").value("Tavastia"))
                .andExpect(jsonPath("$[0].kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$[0].kuvaus").value("Stand-up Comedy Night"))
                .andExpect(jsonPath("$[0].maxLippumaara").value(700))
                .andExpect(jsonPath("$[0].liputJaljella").value(697));
    }

    @Test
    void getTapahtumatByIdReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tapahtumaId").value(1))
                .andExpect(jsonPath("$.jarjestajaId").value(2))
                .andExpect(jsonPath("$.aika").value("2026-10-05T20:00:00"))
                .andExpect(jsonPath("$.paikka").value("Tavastia"))
                .andExpect(jsonPath("$.kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$.kuvaus").value("Stand-up Comedy Night"))
                .andExpect(jsonPath("$.maxLippumaara").value(700))
                .andExpect(jsonPath("$.liputJaljella").value(697));
    }

    @Test
    void getTapahtumatByIdReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/300"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTapahtumatByJarjestajaReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/jarjestaja/3")
                        .with(JARJESTAJA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].tapahtumaId").value(2))
                .andExpect(jsonPath("$[0].jarjestajaId").value(3))
                .andExpect(jsonPath("$[0].aika").value("2026-11-15T19:00:00"))
                .andExpect(jsonPath("$[0].paikka").value("Olympiastadion"))
                .andExpect(jsonPath("$[0].kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$[0].kuvaus").value("Rock Festival"))
                .andExpect(jsonPath("$[0].maxLippumaara").value(40000))
                .andExpect(jsonPath("$[0].liputJaljella").value(39999));
    }

    @Test
    void getTapahtumatByJarjestajaReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/jarjestaja/300"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTapahtumaLiputByTapahtumaReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9/liput")
                .with(JARJESTAJA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$.[0].lippuId").value(1))
                .andExpect(jsonPath("$.[0].tapahtumaId").value(9))
                .andExpect(jsonPath("$.[0].lipputyyppiId").value(1))
                .andExpect(jsonPath("$.[0].myyntitapahtumaId").value(3))
                .andExpect(jsonPath("$.[0].koodi").value("FFFFFF-000006"))
                .andExpect(jsonPath("$.[0].hinta").value(20.00))
                .andExpect(jsonPath("$.[0].status").value("VARATTU"));
    }

    @Test
    void getTapahtumaLiputByTapahtumaReturnsOkAsAdmin() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9/liput")
                        .with(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(11));
    }

    @Test
    void getTapahtumaLiputByTapahtumaReturnsForbidden() throws Exception {
        mockMvc.perform(get(BASE_URL + "/9/liput")
                        .with(JARJESTAJA1))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTapahtumaReturnsCreated() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2027-01-01T00:00:00",
                                  "paikka": "Kansalaistori",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Uusivuosi",
                                  "maxLippumaara": 1000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tapahtumaId").value(12))
                .andExpect(jsonPath("$.jarjestajaId").value(2))
                .andExpect(jsonPath("$.aika").value("2027-01-01T00:00:00"))
                .andExpect(jsonPath("$.paikka").value("Kansalaistori"))
                .andExpect(jsonPath("$.kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$.kuvaus").value("Uusivuosi"))
                .andExpect(jsonPath("$.maxLippumaara").value(1000))
                .andExpect(jsonPath("$.liputJaljella").value(1000));
    }

    @Test
    void createTapahtumaReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2027-01-01T00:00:00",
                                  "paikka": "",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "",
                                  "maxLippumaara": 1000
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(BASE_URL)
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2027-01-01T00:00:00",
                                  "paikka": "joku",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "kuvaus",
                                  "maxLippumaara": 0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyTapahtumaReturnsOk() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2")
                        .with(JARJESTAJA1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2026-11-15T19:00:00",
                                  "paikka": "Olympiastadion",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Rock Festival",
                                  "maxLippumaara": 30000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tapahtumaId").value(2))
                .andExpect(jsonPath("$.jarjestajaId").value(3))
                .andExpect(jsonPath("$.aika").value("2026-11-15T19:00:00"))
                .andExpect(jsonPath("$.paikka").value("Olympiastadion"))
                .andExpect(jsonPath("$.kaupunki").value("Helsinki"))
                .andExpect(jsonPath("$.kuvaus").value("Rock Festival"))
                .andExpect(jsonPath("$.maxLippumaara").value(30000))
                .andExpect(jsonPath("$.liputJaljella").value(29999));
    }

    @Test
    void modifyTapahtumaReturnsBadRequest() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2")
                        .with(JARJESTAJA1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "",
                                  "paikka": "Olympiastadion",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Rock Festival",
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyTapahtumaReturnsNotFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/333")
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2026-11-15T19:00:00",
                                  "paikka": "Olympiastadion",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Rock Festival",
                                  "maxLippumaara": 30000
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void modifyTapahtumaReturnsConflict() throws Exception {
        // tapahtuman maxLippumaara on pienempi kuin myytyjen lippujen määrä
        mockMvc.perform(put(BASE_URL + "/9")
                        .with(JARJESTAJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "aika": "2027-08-01T18:00:00",
                                  "paikka": "test",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "kapasiteettitesti",
                                  "maxLippumaara": 9,
                                  "poistettu": false
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteTapahtumaReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3")
                        .with(JARJESTAJA))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTapahtumaReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .with(JARJESTAJA))
                .andExpect(status().isNotFound());
    }
}