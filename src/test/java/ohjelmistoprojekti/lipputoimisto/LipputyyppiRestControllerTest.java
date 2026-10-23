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

// TODO: testaa myös tapahtuman omistajuus

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class LipputyyppiRestControllerTest {
    private static final String BASE_URL = "/api/tapahtumat";

    private static final RequestPostProcessor MYYJA =
            httpBasic("myyja", "password");
    private static final RequestPostProcessor JARJESTAJA =
            httpBasic("jarjestaja", "password");
    private static final RequestPostProcessor ADMIN =
            httpBasic("admin", "password");

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
                .andExpect(jsonPath("$[0].liputJaljella").doesNotExist())

                .andExpect(jsonPath("$[1].lipputyyppiId").value(2))
                .andExpect(jsonPath("$[1].kuvaus").value("VIP"))
                .andExpect(jsonPath("$[1].lipunHinta").value(20.00))
                .andExpect(jsonPath("$[1].maxLippumaara").value(10))
                .andExpect(jsonPath("$[1].liputJaljella").value(10));

        mockMvc.perform(get(BASE_URL + "/9/lipputyypit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lipputyyppiId").value(1))
                .andExpect(jsonPath("$[0].kuvaus").value("varatut liput"))
                .andExpect(jsonPath("$[0].lipunHinta").value(20.00))
                .andExpect(jsonPath("$[0].maxLippumaara").value(20))
                .andExpect(jsonPath("$[0].liputJaljella").value(10));

        // poistettujen lipputyyppien ei pitäisi näkyä
        mockMvc.perform(get(BASE_URL + "/10/lipputyypit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[?(@.lipputyyppiId == 1)]").isEmpty())
                .andExpect(jsonPath("$[0].lipputyyppiId").value(2))
                .andExpect(jsonPath("$[0].kuvaus").value("ei poistettu"))
                .andExpect(jsonPath("$[0].lipunHinta").value(10.00))
                .andExpect(jsonPath("$[0].maxLippumaara").doesNotExist())
                .andExpect(jsonPath("$[0].liputJaljella").doesNotExist());
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
                        .with(JARJESTAJA)
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
        mockMvc.perform(post(BASE_URL + "/3/lipputyypit")
                        .with(JARJESTAJA)
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
                .andExpect(jsonPath("$.maxLippumaara").value(500))
                .andExpect(jsonPath("$.liputJaljella").value(500));
    }

    @Test
    void createLipputyyppiReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_URL + "/2/lipputyypit")
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
        mockMvc.perform(put(BASE_URL + "/3/lipputyypit/2")
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                .andExpect(jsonPath("$.maxLippumaara").value(400))
                .andExpect(jsonPath("$.liputJaljella").value(400));


        // maxLippumaara nulliksi
        mockMvc.perform(put(BASE_URL + "/6/lipputyypit/1")
                        .with(JARJESTAJA)
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
                .andExpect(jsonPath("$.maxLippumaara").doesNotExist())
                .andExpect(jsonPath("$.liputJaljella").doesNotExist());

    }

    @Test
    void modifyLipputyyppiReturnsBadRequest() throws Exception {
        mockMvc.perform(put(BASE_URL + "/2/lipputyypit/2")
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA)
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
                        .with(JARJESTAJA))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLipputyyppiReturnsNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/3/lipputyypit/99")
                        .with(JARJESTAJA))
                .andExpect(status().isNotFound());
    }
}
