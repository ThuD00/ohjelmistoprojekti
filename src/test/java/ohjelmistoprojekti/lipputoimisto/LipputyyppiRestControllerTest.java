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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO: tarkista käyttöoikeudet
@SpringBootTest
@AutoConfigureMockMvc
public class LipputyyppiRestControllerTest {
    private static final String BASE_URL = "/api/tapahtumat";

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
        // ilman maxLippumaara
        mockMvc.perform(get(BASE_URL + "/1/lipputyypit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lipputyyppiId").value(1))
                .andExpect(jsonPath("$[0].kuvaus").value("Aikuinen"))
                .andExpect(jsonPath("$[0].lipunHinta").value(15.00))
                .andExpect(jsonPath("$[1].lipputyyppiId").value(2))
                .andExpect(jsonPath("$[1].kuvaus").value("Lapsi"))
                .andExpect(jsonPath("$[1].lipunHinta").value(7.50))
                .andExpect(jsonPath("$[2].lipputyyppiId").value(3))
                .andExpect(jsonPath("$[2].kuvaus").value("Opiskelija"))
                .andExpect(jsonPath("$[2].lipunHinta").value(10.00));

        mockMvc.perform(get(BASE_URL + "/7/lipputyypit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lipputyyppiId").value(1))
                .andExpect(jsonPath("$[0].kuvaus").value("Aikuinen"))
                .andExpect(jsonPath("$[0].lipunHinta").value(10.00))
                .andExpect(jsonPath("$[0].maxLippumaara").doesNotExist())
                .andExpect(jsonPath("$[1].lipputyyppiId").value(2))
                .andExpect(jsonPath("$[1].kuvaus").value("VIP"))
                .andExpect(jsonPath("$[1].lipunHinta").value(20.00))
                .andExpect(jsonPath("$[1].maxLippumaara").value(10));
    }

    @Test
    void getLipputyypitReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "/99/lipputyypit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLipputyyppiReturnsCreated() throws Exception {
        // ilman maxLippumaara
        mockMvc.perform(post(BASE_URL + "/3/lipputyypit")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Eläkeläinen",
                                  "lipunHinta": 5.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lipputyyppiId").value(3))
                .andExpect(jsonPath("$.kuvaus").value("Eläkeläinen"))
                .andExpect(jsonPath("$.lipunHinta").value(5.00));

        // maxLippumaara
        mockMvc.perform(post(BASE_URL + "/2/lipputyypit")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "VIP",
                                  "lipunHinta": 80.00,
                                  "maxLippumaara": 500
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lipputyyppiId").value(3))
                .andExpect(jsonPath("$.kuvaus").value("VIP"))
                .andExpect(jsonPath("$.lipunHinta").value(80.00))
                .andExpect(jsonPath("$.maxLippumaara").value(500));
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
    void createLipputyyppiReturnsConflict() throws Exception {
        // ylittää koko tapahtuman maxLippumaara
        mockMvc.perform(post(BASE_URL + "/6/lipputyypit")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Iltabilelippu",
                                  "lipunHinta": 17.00,
                                  "maxLippumaara": 3000
                                }
                                """))
                .andExpect(status().isConflict());

        // ylittää muiden lipputyyppien kanssa maxLippumaara
        mockMvc.perform(post(BASE_URL + "/8/lipputyypit")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Premium",
                                  "lipunHinta": 200.00,
                                  "maxLippumaara": 601
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void modifyLipputyyppiReturnsOk() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/lipputyypit/2")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Aikuinen",
                                  "lipunHinta": 60.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lipputyyppiId").value(2))
                .andExpect(jsonPath("$.kuvaus").value("Aikuinen"))
                .andExpect(jsonPath("$.lipunHinta").value(60.00));
    }

    @Test
    void modifyLipputyyppiReturnsOkWithMaxLippumaara() throws Exception {
        mockMvc.perform(put(BASE_URL + "/8/lipputyypit/1")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Standard",
                                  "lipunHinta": 175.00,
                                  "maxLippumaara": 400
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lipputyyppiId").value(1))
                .andExpect(jsonPath("$.kuvaus").value("Standard"))
                .andExpect(jsonPath("$.lipunHinta").value(175.00))
                .andExpect(jsonPath("$.maxLippumaara").value(400));

        // maxLippumaara nulliksi
        mockMvc.perform(put(BASE_URL + "/6/lipputyypit/1")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Tapahtumalippu",
                                  "lipunHinta": 39.00,
                                  "maxLippumaara": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lipputyyppiId").value(1))
                .andExpect(jsonPath("$.kuvaus").value("Tapahtumalippu"))
                .andExpect(jsonPath("$.lipunHinta").value(39.00))
                .andExpect(jsonPath("$.maxLippumaara").doesNotExist());
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
                        .with(ADMIN)
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
    void modifyLipputyyppiReturnsConflict() throws Exception {
        // muiden lipputyyppien maxLippumaara yhteenlaskettuna ylittää tapahtuman maxLippumaara
        mockMvc.perform(put(BASE_URL + "/8/lipputyypit/1")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "Standard",
                                  "lipunHinta": 150.00,
                                  "maxLippumaara": 901
                                }
                                """))
                .andExpect(status().isConflict());

        // lipputyypin maxLippumaara ei saa olla pienempi kuin jo myytyjen lippujen määrä
        mockMvc.perform(put(BASE_URL + "/9/lipputyypit/1")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "varatut liput",
                                  "lipunHinta": 20.00,
                                  "maxLippumaara": 9
                                }
                                """))
                .andExpect(status().isConflict());

        mockMvc.perform(put(BASE_URL + "/9/lipputyypit/1")
                        .with(ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "kuvaus": "varatut liput",
                                  "lipunHinta": 20.00,
                                  "maxLippumaara": 9
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteLipputyyppiReturnsNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3/lipputyypit/1")
                        .with(ADMIN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLipputyyppiReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3/lipputyypit/99")
                        .with(ADMIN))
                .andExpect(status().isNotFound());
    }
}
