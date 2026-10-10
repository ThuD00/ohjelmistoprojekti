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
public class LipputyyppiRestControllerTest {
    private static final String BASE_URL = "/api/tapahtumat";

    // TODO: lisää testeihin järjestäjäId yms kun semmonen on lisätty ja käytä testeissä oikeaa käyttäjää
    private static final RequestPostProcessor USER1 =
            httpBasic("user", "user");
    private static final RequestPostProcessor USER2 =
            httpBasic("user2", "");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "admin");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getLipputyypitReturnsOk() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1/lipputyypit"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "lipputyyppiId": 1,
                            "kuvaus": "Aikuinen",
                            "lipunHinta": 15.00
                          },
                          {
                            "lipputyyppiId": 2,
                            "kuvaus": "Lapsi",
                            "lipunHinta": 7.50
                          },
                          {
                            "lipputyyppiId": 3,
                            "kuvaus": "Opiskelija",
                            "lipunHinta": 10.00
                          }
                        ]
                        """));
    }

    @Test
    void getLipputyypitReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/99/lipputyypit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLipputyyppiReturnsCreated() throws Exception {
        mockMvc.perform(post(BASE_URL + "/2/lipputyypit")
                        .with(USER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "VIP",
                                  "lipunHinta": 80.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(
                        content().json("""
                                {
                                  "lipputyyppiId": 3,
                                  "kuvaus": "VIP",
                                  "lipunHinta": 80.00
                                }
                                """));
    }

    @Test
    void createLipputyyppiReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/2/lipputyypit")
                        .with(USER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Eläkeläinen",
                                  "lipunHinta":
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyLipputyyppiReturnsOk() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/lipputyypit/2")
                        .with(USER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Aikuinen",
                                  "lipunHinta": 60.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("""
                                {
                                  "lipputyyppiId": 2,
                                  "kuvaus": "Aikuinen",
                                  "lipunHinta": 60.00
                                }
                                """));
    }

    @Test
    void modifyLipputyyppiReturnsBadRequest() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/lipputyypit/2")
                        .with(USER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "",
                                  "lipunHinta": 60.00
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modifyLipputyyppiReturnsNotFound() throws Exception {
        mockMvc.perform(put(BASE_URL + "/1/lipputyypit/4")
                        .with(USER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Eläkeläinen",
                                  "lipunHinta": 10.00
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLipputyyppiReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3/lipputyypit/1")
                        .with(ADMIN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLipputyyppiReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "3/lipputyypit/99")
                        .with(ADMIN))
                .andExpect(status().isNotFound());
    }
}
