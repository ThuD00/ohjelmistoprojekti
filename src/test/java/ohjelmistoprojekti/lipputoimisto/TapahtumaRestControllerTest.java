package ohjelmistoprojekti.lipputoimisto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TapahtumaRestControllerTest {

    private static final RequestPostProcessor USER1 =
            httpBasic("user1", "");
    private static final RequestPostProcessor USER2 =
            httpBasic("user2", "");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTapahtumatReturnsOk() throws Exception {
        mockMvc.perform(get("/tapahtumat"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                      [
                          {
                            "tapahtumaId": 1,
                            "aika": "2026-10-05T20:00:00",
                            "paikka": "Tavastia",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Stand-up Comedy Night",
                            "maxLippumaara": 700,
                            "lipputyypit": [
                              {
                                "kuvaus": "Aikuinen",
                                "lipunHinta": 15.00,
                                "id": {
                                  "tapahtumaId": 1,
                                  "lipputyyppiId": 1
                                }
                              },
                              {
                                "kuvaus": "Lapsi",
                                "lipunHinta": 7.50,
                                "id": {
                                  "tapahtumaId": 1,
                                  "lipputyyppiId": 2
                                }
                              },
                              {
                                "kuvaus": "Opiskelija",
                                "lipunHinta": 10.00,
                                "id": {
                                  "tapahtumaId": 1,
                                  "lipputyyppiId": 3
                                }
                              }
                            ]
                          },
                          {
                            "tapahtumaId": 2,
                            "aika": "2026-11-15T19:00:00",
                            "paikka": "Olympiastadion",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Rock Festival",
                            "maxLippumaara": 40000,
                            "lipputyypit": [
                              {
                                "kuvaus": "Aikuinen",
                                "lipunHinta": 45.00,
                                "id": {
                                  "tapahtumaId": 2,
                                  "lipputyyppiId": 1
                                }
                              },
                              {
                                "kuvaus": "Opiskelija",
                                "lipunHinta": 30.00,
                                "id": {
                                  "tapahtumaId": 2,
                                  "lipputyyppiId": 2
                                }
                              }
                            ]
                          },
                          {
                            "tapahtumaId": 3,
                            "aika": "2026-12-10T18:00:00",
                            "paikka": "Finlandia-talo",
                            "kaupunki": "Helsinki",
                            "kuvaus": "Super-Gaala",
                            "maxLippumaara": 1700,
                            "lipputyypit": [
                              {
                                "kuvaus": "Aikuinen",
                                "lipunHinta": 60.00,
                                "id": {
                                  "tapahtumaId": 3,
                                  "lipputyyppiId": 1
                                }
                              },
                              {
                                "kuvaus": "Opiskelija",
                                "lipunHinta": 40.00,
                                "id": {
                                  "tapahtumaId": 3,
                                  "lipputyyppiId": 2
                                }
                              }
                            ]
                          }
                                                ]
                        """));
    }

    void getTapahtumatByIdReturnsOk() throws Exception {
        mockMvc.perform(get("/tapahtumat/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getTapahtumatReturnsO() throws Exception {
        mockMvc.perform(get("/tapahtumat")
                        .with(httpBasic("sa", "")))
                .andExpect(status().isOk());
    }
}