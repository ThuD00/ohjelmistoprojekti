package ohjelmistoprojekti.lipputoimisto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TapahtumaRestControllerTest {

    private static final String BASE_URL = "/api/tapahtumat";

    private static final RequestPostProcessor USER1 =
            httpBasic("user1", "");
    private static final RequestPostProcessor USER2 =
            httpBasic("user2", "");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "");

    @Autowired
    private MockMvc mockMvc;

    // TODO: testaa myös tapahtuman omistajuus

    @Test
    void getTapahtumatReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "tapahtumaId": 1,
                            "jarjestajaId": 1,
                            "aika": "2026-10-05T20:00:00",
                            "paikka": "Tavastia",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Stand-up Comedy Night",
                            "maxLippumaara": 700
                          },
                          {
                            "tapahtumaId": 2,
                            "jarjestajaId": 2,
                            "aika": "2026-11-15T19:00:00",
                            "paikka": "Olympiastadion",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Rock Festival",
                            "maxLippumaara": 40000
                          },
                          {
                            "tapahtumaId": 3,
                            "jarjestajaId": 1,
                            "aika": "2026-12-10T18:00:00",
                            "paikka": "Finlandia-talo",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Super-Gaala",
                            "maxLippumaara": 1700
                          }
                        ]
                        """));
    }

    @Test
    void getTapahtumatByIdReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "tapahtumaId": 1,
                          "jarjestajaId": 1,
                          "aika": "2026-10-05T20:00:00",
                          "paikka": "Tavastia",
                          "kaupunki": "Helsinki",
                          "kuvaus": "Stand-up Comedy Night",
                          "maxLippumaara": 700
                        }
                        """));
    }

    @Test
    void getTapahtumatByIdReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/300"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTapahtumaReturnsCreated() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(USER1)
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
                .andExpect(
                        content().json("""
                                {
                                  "tapahtumaId": 4,
                                  "aika": "2027-01-01T00:00:00",
                                  "paikka": "Kansalaistori",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Uusivuosi",
                                  "maxLippumaara": 1000
                                }
                                """));
    }

    @Test
    void createTapahtumaReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(USER1)
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
    }

    @Test
    void modifyTapahtumaReturnsOk() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2")
                        .with(USER1)
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
                .andExpect(
                        content().json("""
                                {
                                  "tapahtumaId": 2,
                                  "jarjestajaId": 2,
                                  "aika": "2026-11-15T19:00:00",
                                  "paikka": "Olympiastadion",
                                  "kaupunki": "Helsinki",
                                  "kuvaus": "Rock Festival",
                                  "maxLippumaara": 30000
                                }
                                """));
    }

    @Test
    void modifyTapahtumaReturnsBadRequest() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2")
                        .with(USER1)
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
                        .with(USER1)
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
    void deleteTapahtumaReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3")
                .with(ADMIN))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteTapahtumaReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/99")
                        .with(ADMIN))
                .andExpect(status().isNotFound());
    }
}