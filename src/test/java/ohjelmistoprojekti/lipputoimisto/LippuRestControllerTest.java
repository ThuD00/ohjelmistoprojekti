package ohjelmistoprojekti.lipputoimisto;

import ohjelmistoprojekti.lipputoimisto.domain.Lippu;
import ohjelmistoprojekti.lipputoimisto.repository.LippuRepository;
import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LippuRestControllerTest {
    private static final String BASE_URL = "/api/liput";

    private static final RequestPostProcessor MYYJA =
            httpBasic("myyja", "password");
    private static final RequestPostProcessor JARJESTAJA =
            httpBasic("jarjestaja", "password");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "password");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LippuRepository lippuRepository;

    @Test
    void reserveTicketReturnsCreated() throws Exception {
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 1,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 2
                                        },
                                        {
                                            "lipputyyppiId": 2,
                                            "qty": 1
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.myyntitapahtumaId").value(6))
                .andExpect(jsonPath("$.summa").value(37.5))
                .andExpect(jsonPath("$.liput").isArray())
                .andExpect(jsonPath("$.liput.length()").value(3))

                .andExpect(jsonPath("$.liput[0].lipputyyppiId").value(1))
                .andExpect(jsonPath("$.liput[0].koodi").isNotEmpty())

                .andExpect(jsonPath("$.liput[1].lipputyyppiId").value(1))
                .andExpect(jsonPath("$.liput[1].koodi").isNotEmpty())

                .andExpect(jsonPath("$.liput[2].lipputyyppiId").value(2))
                .andExpect(jsonPath("$.liput[2].koodi").isNotEmpty());
        // TODO: testaa että liput oikeasti on varattu
    }

    @Test
    void reserveTicketReturnsNotFound() throws Exception {
        // tapahtumaId ei löydy
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 33,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 1
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isNotFound());
        // lipputyyppiId ei löydy
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 1,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 5,
                                            "qty": 2
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isNotFound());
        // tapahtuma on poistettu
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 5,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 2
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isNotFound());
        // lipputyyppi on poistettu
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 10,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 2
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void reserveTicketReturnsBadRequest() throws Exception {
        // ilman lippuja
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 1,
                                    "liput": []
                                }
                                """))
                .andExpect(status().isBadRequest());
        // negatiivinen määrä
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 1,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": -2
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isBadRequest());
        // lipputyyppiId puuttuu
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 1,
                                    "liput": [
                                        {
                                            "qty": 1
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isBadRequest());
        // tapahtumaId puuttuu
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "liput": [
                                        {
                                            "qty": 2
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reserveTicketExceedsMaxLippumaaraCapacityReturnsConflict() throws Exception {
        // tapahtuma.maxLippumaara ylittyy
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 7,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 91
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isConflict());
        // lipputyyppi.maxLippumaara ylittyy
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 7,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 2,
                                            "qty": 11
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isConflict());
        // molemmat ylittyy
        mockMvc.perform(post(BASE_URL + "/varaa")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "tapahtumaId": 7,
                                    "liput": [
                                        {
                                            "lipputyyppiId": 1,
                                            "qty": 91
                                        },
                                        {
                                            "lipputyyppiId": 2,
                                            "qty": 11
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void redeemTicketReturnsOk() throws Exception {
        mockMvc.perform(post(BASE_URL + "/lunasta")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "ABCDEF-123456"}
                                """))
                .andExpect(status().isOk());

        Lippu lippu = lippuRepository.findByKoodi("ABCDEF-123456").orElseThrow();
        Assertions.assertEquals(Lippu.LippuTila.LUNASTETTU, lippu.getLipunStatus());
    }

    @Test
    void redeemTicketReturnsNotFound() throws Exception {
        mockMvc.perform(post(BASE_URL + "/lunasta")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "AAAAAA-111111"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void redeemTicketReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/lunasta")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void redeemTicketReturnsConflict() throws Exception {
        // lippu on jo lunastettu
        mockMvc.perform(post(BASE_URL + "/lunasta")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "FEDCBA-654321"}
                                """))
                .andExpect(status().isConflict());
        // lippu on peruttu
        mockMvc.perform(post(BASE_URL + "/lunasta")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "AABBCC-112233"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void cancelTicketReturnsOk() throws Exception {
        mockMvc.perform(post(BASE_URL + "/peru")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "CCBBAA-332211"}
                                """))
                .andExpect(status().isOk());
        Lippu lippu = lippuRepository.findByKoodi("CCBBAA-332211").orElseThrow();
        Assertions.assertEquals(Lippu.LippuTila.PERUTTU, lippu.getLipunStatus());
    }

    @Test
    void cancelTicketReturnsNotFound() throws Exception {
        mockMvc.perform(post(BASE_URL + "/peru")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "AAAAAA-111111"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelTicketReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/peru")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelTicketReturnsConflict() throws Exception {
        // lippu on lunastettu
        mockMvc.perform(post(BASE_URL + "/peru")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "FEDCBA-654321"}
                                """))
                .andExpect(status().isConflict());
        // lippu on jo peruttu
        mockMvc.perform(post(BASE_URL + "/peru")
                        .with(MYYJA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"koodi": "AABBCC-112233"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void generateQrCodeReturnsOk() throws Exception {
        mockMvc.perform(post(BASE_URL + "/qr")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"koodi": "ABCDEF-123456"}
                        """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

}
